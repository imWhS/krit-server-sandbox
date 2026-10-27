package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.domain.PostTag;
import imwhs.krit_server_sandbox.domain.Tag;
import imwhs.krit_server_sandbox.dto.post.PostBaseInfo;
import imwhs.krit_server_sandbox.dto.post.PostInfo;
import imwhs.krit_server_sandbox.dto.post.PostInfoCursorResponse;
import imwhs.krit_server_sandbox.dto.post.PostTagDto;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.exception.post.PostNotFoundException;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import imwhs.krit_server_sandbox.repository.PostRepository;
import imwhs.krit_server_sandbox.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;

    @Transactional
    public PostInfo register(Long authorId, String title, String content, Set<String> tagNames) {
        Account author = accountRepository.findByIdAndDeletedAtIsNull(authorId)
                .orElseThrow(() -> new AccountNotFoundException(authorId));

        Post post = Post.create(author, title, content);

        if (tagNames != null) {
            Set<String> tagNamesStripped = normalizeTagNames(tagNames);

            // tagNames 중 tagName을 name으로 가지고 있는 Tag만 조회합니다.
            Map<String, Tag> tagMap = createTagMapWithNotExistingTagNames(tagNamesStripped);

            // Post.tags를 통해 Post와 연관 관계를 설정합니다.
            for (Tag tag : tagMap.values()) {
                post.addTag(tag);
            }
        }

        // Post 및 Post와 연관 관계가 설정된 모든 PostTag를 함께 영속화합니다.
        postRepository.save(post);

        return PostInfo.from(post);
    }

    public PostInfo getInfo(Long id) {
        Post post = getWithAuthorTags(id);
        return PostInfo.from(post);
    }

    /**
     * 커서를 기준으로 페이징된 Post 목록을 조회합니다.
     * @param cursor 커서
     * @param size 페이징 크기
     * @return 커서를 기준으로 페이징된 Post 목록 및 커서 페이징 정보
     */
    public PostInfoCursorResponse getAllInfosByCursor(Long cursor, int size) {
        if (size < 10 || 100 < size) {
            throw new IllegalArgumentException("페이징 크기는 10 이상 100 이하 사이의 자연수로만 설정할 수 있어요.");
        }

        Pageable pageable = PageRequest.of(0, size);
        Slice<PostBaseInfo> baseInfos;

        if (cursor == null) {
            baseInfos = postRepository.findFirstBaseInfosByDeletedAtIsNull(pageable);
        } else {
            baseInfos = postRepository.findNextBaseInfosByCursorDeletedAtIsNull(cursor, pageable);
        }

        if (baseInfos.isEmpty()) {
            return new PostInfoCursorResponse(List.of(), false, null);
        }

        Set<Long> postIds = baseInfos.stream()
                .map(baseInfo -> baseInfo.id())
                .collect(Collectors.toSet());

        List<PostTagDto> postTagDtos = postRepository.findAllTagNamesByPostIds(postIds);
        Map<Long, Set<String>> tagNamesByPostId = postTagDtos.stream()
                .collect(Collectors.groupingBy(
                        postTagDto -> postTagDto.postId(),
                        Collectors.mapping(postTagDto -> postTagDto.tagName(), Collectors.toSet())));

        List<PostInfo> postInfos = baseInfos.stream()
                .map(baseInfo -> PostInfo.from(baseInfo, tagNamesByPostId.get(baseInfo.id())))
                .toList();

        Long nextCursor = null;
        if (baseInfos.hasNext()) {
            nextCursor = postInfos.get(postInfos.size() - 1).id();
        }

        return new PostInfoCursorResponse(postInfos, baseInfos.hasNext(), nextCursor);
    }

    /**
     * 모든 Post 목록을 조회합니다.
     * 필요한 경우, 페이징을 적용할 수 있습니다.
     * @param pageable 페이징 정보
     * @return 모든 Post 목록
     */
    public Page<PostInfo> getAllInfos(Pageable pageable) {
        Page<PostBaseInfo> baseInfos = postRepository.findAllBaseInfosByDeletedAtIsNull(pageable);

        if (baseInfos.isEmpty()) {
            return baseInfos.map(baseInfo -> PostInfo.from(baseInfo, Collections.emptySet()));
        }

        Set<Long> postIds = baseInfos.stream()
                .map(baseInfo -> baseInfo.id())
                .collect(Collectors.toSet());

        // Post ID로 PostTag 필터링
        List<PostTagDto> tagNamesByPostId = postRepository.findAllTagNamesByPostIds(postIds);

        Map<Long, Set<String>> tagNamesByPostIdMap = tagNamesByPostId.stream()
                .collect(Collectors.groupingBy(
                        postTagDto -> postTagDto.postId(),
                        Collectors.mapping(postTagDto -> postTagDto.tagName(), Collectors.toSet())));

        List<PostInfo> postInfos = baseInfos.stream()
                .map(baseInfo -> {
                    Long id = baseInfo.id();
                    Set<String> tagNames = tagNamesByPostIdMap.getOrDefault(id, Collections.emptySet());
                    return PostInfo.from(baseInfo, tagNames);
                })
                .toList();

        return new PageImpl<>(postInfos, pageable, baseInfos.getTotalElements());
    }

    /**
     * Post를 업데이트합니다.
     * @param id 업데이트할 Post의 ID
     * @param title Post의 새 제목
     * @param content Post의 새 본문
     * @param tagNames Post에 새로 달 Tag의 이름 목록.
     *                 게시물의 태그 목록(Post.tags)에서 tagNames에 포함되지 않은 이름에 해당하는 PostTag는 삭제됩니다.
     */
    @Transactional
    public void update(Long id, String title, String content, Set<String> tagNames) {
        Post post = getWithAuthorTags(id);
        post.updateTitle(title);
        post.updateContent(content);

        if (Objects.isNull(tagNames)) { return; }

        Set<String> tagNamesStripped = normalizeTagNames(tagNames);
        Set<String> tagNamesToAdd = new HashSet<>(tagNamesStripped);

        Iterator<PostTag> iterator = post.getTags().iterator();
        while (iterator.hasNext()) {
            PostTag postTag = iterator.next();
            String existingTagName = postTag.getTag().getName();

            if (tagNamesStripped.contains(existingTagName)) {
                tagNamesToAdd.remove(existingTagName);
                continue;
            }

            // tagNames에 포함되어 있지 않은 Post의 tagName은 Post에서 지웁니다.
            iterator.remove();
        }

        // tagNames 중 tagName을 name으로 가지고 있는 Tag만 조회합니다.
        Map<String, Tag> tagMap = createTagMapWithNotExistingTagNames(tagNamesToAdd);

        // Tag에 대한 PostTag를 생성한 후 업데이트할 Post와 연관 관계를 설정합니다.
        for (Tag tag : tagMap.values()) {
            post.addTag(tag);
        }
    }

    @Transactional
    public void updateTitle(Long id, String title) {
        Post post = get(id);
        post.updateTitle(title);
    }

    @Transactional
    public void updateContent(Long id, String content) {
        Post post = get(id);
        post.updateContent(content);
    }

    @Transactional
    public void softDelete(Long id, Long actorId) {
        Account actor = accountRepository.findByIdAndDeletedAtIsNull(actorId)
                .orElseThrow(() -> new AccountNotFoundException(actorId));
        Post post = get(id);
        post.softDelete(actor.getId());
    }

    private Post get(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    private Post getWithAuthorTags(Long id) { // getWithAuthor에서 변경
        return postRepository.findWithAuthorTagsByIdDeletedAtIsNull(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    private Map<String, Tag> createTagMapWithNotExistingTagNames(Set<String> tagNames) { // 메서드 이름이 좀 밤티
        Set<Tag> existingTags = tagRepository.findAllByNameIn(tagNames);
        Map<String, Tag> tagMap = existingTags.stream()
                .collect(Collectors.toMap(Tag::getName, item -> item));

        for (String tagName : tagNames) {
            if (tagMap.containsKey(tagName)) { continue; }
            // tagName을 name으로 가지고 있는 Tag가 없으면 Tag를 생성합니다.
            Tag tag = Tag.create(tagName);
            tagRepository.save(tag);
            tagMap.put(tagName, tag);
        }

        return tagMap;
    }

    private static Set<String> normalizeTagNames(Set<String> tagNames) {
        Set<String> tagNamesStripped = tagNames.stream()
                .filter(tagName -> Objects.nonNull(tagName))
                .map(String::strip)
                .collect(Collectors.toSet());
        return tagNamesStripped;
    }

}
