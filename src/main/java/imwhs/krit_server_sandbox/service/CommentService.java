package imwhs.krit_server_sandbox.service;

import imwhs.krit_server_sandbox.domain.Account;
import imwhs.krit_server_sandbox.domain.Comment;
import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.dto.comment.CommentInfo;
import imwhs.krit_server_sandbox.exception.account.AccountNotFoundException;
import imwhs.krit_server_sandbox.exception.comment.CommentNotFoundException;
import imwhs.krit_server_sandbox.exception.post.PostNotFoundException;
import imwhs.krit_server_sandbox.repository.AccountRepository;
import imwhs.krit_server_sandbox.repository.CommentRepository;
import imwhs.krit_server_sandbox.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final AccountRepository accountRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentInfo register(Long authorId, Long postId, String body) {
        Account author = accountRepository.findByIdAndDeletedAtIsNull(authorId).orElseThrow(
                () -> new AccountNotFoundException(authorId));
        Post post = postRepository.findByIdAndDeletedAtIsNull(postId).orElseThrow(
                () -> new PostNotFoundException(postId));

        Comment comment = Comment.create(author, post, body);
        commentRepository.save(comment);
        return CommentInfo.from(comment);
    }

    public CommentInfo getInfo(Long id) {
        Comment comment = commentRepository.findWithAuthorByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new CommentNotFoundException(id));
        return CommentInfo.from(comment);
    }

    @Transactional
    public void updateBody(Long authorId, Long id, String body) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new CommentNotFoundException(id));

        if (!comment.getAuthor().getId().equals(authorId)) {
            throw new AccessDeniedException("댓글의 작성자가 아니어서, 댓글을 수정할 권한이 없어요.");
        }

        comment.updateBody(body);
    }

    @Transactional
    public void softDelete(Long id, Long actorId) {
        Comment comment = commentRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(
                () -> new CommentNotFoundException(id));

        if (comment.isSoftDeleted()) {
            throw new IllegalArgumentException("이미 논리적 삭제 처리된 댓글이에요.");
        }

        comment.softDelete(actorId);
    }

}
