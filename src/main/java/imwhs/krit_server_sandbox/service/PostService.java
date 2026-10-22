package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.dto.PostInfo;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.exception.post.PostNotFoundException;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import imwhs.krit_server_sandbox.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public PostInfo register(Long authorId, String title, String content) {
        Account author = accountRepository.findByIdAndDeletedAtIsNull(authorId).orElseThrow(() ->
                new AccountNotFoundException(authorId));
        Post post = Post.create(author, title, content);
        postRepository.save(post);
        return PostInfo.from(post);
    }

    public PostInfo getInfo(Long id) {
        Post post = getWithAuthor(id);
        return PostInfo.from(post);
    }

    public Page<PostInfo> getAllInfos(Pageable pageable) {
        Page<Post> posts = postRepository.findAllWithAuthorByDeletedAtIsNull(pageable);
        return posts.map(PostInfo::from);
    }

    @Transactional
    public void update(Long id, String title, String content) {
        Post post = get(id);
        post.updateTitle(title);
        post.updateContent(content);
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
        Account actor = accountRepository.findByIdAndDeletedAtIsNull(actorId).orElseThrow(() ->
                new AccountNotFoundException(actorId));
        Post post = get(id);
        post.softDelete(actor.getId());
    }

    private Post get(Long id) {
        return postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    private Post getWithAuthor(Long id) {
        return postRepository.findWithAuthorByIdDeletedAtIsNull(id).orElseThrow(() -> new PostNotFoundException(id));
    }

}
