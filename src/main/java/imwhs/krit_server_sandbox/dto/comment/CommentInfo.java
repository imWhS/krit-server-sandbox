package imwhs.krit_server_sandbox.dto.comment;

import imwhs.krit_server_sandbox.domain.Comment;

public record CommentInfo(
        Long id,
        String body,
        String authorName
) {
    public static CommentInfo from(Comment comment) {
        return new CommentInfo(
                comment.getId(),
                comment.getBody(),
                comment.getAuthor().getName());
    }
}
