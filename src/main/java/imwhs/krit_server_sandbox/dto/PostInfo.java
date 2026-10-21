package imwhs.krit_server_sandbox.dto;

import imwhs.krit_server_sandbox.domain.Post;

public record PostInfo(
        Long id,
        String title,
        String content,
        String authorName
) {
    public static PostInfo from(Post post) {
        return new PostInfo(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getName());
    }
}