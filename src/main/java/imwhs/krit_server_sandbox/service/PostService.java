package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import imwhs.krit_server_sandbox.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public Post register(Long authorId, String title, String content) {
        Account author = accountRepository.findById(authorId).orElseThrow(() ->
                new IllegalArgumentException("작성자의 ID가 유효하지 않아요."));
        Post post = Post.create(author, title, content);
        postRepository.save(post);
        return post;
    }

    public Post get(Long id) {
        Post post = postRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
                new NoSuchElementException("ID가 " + id + "인 게시물을 찾을 수 없어요."));
        return post;
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
        Account actor = accountRepository.findById(actorId).orElseThrow(() ->
                new IllegalArgumentException("게시물 삭제를 요청한 계정의 ID가 유효하지 않아요."));
        Post post = get(id);
        post.softDelete(actor.getId());
    }

}
