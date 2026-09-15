package imwhs.krit_server_sandbox.dto;

import imwhs.krit_server_sandbox.domain.Post;

public record PostResponse(
    Long id,
    String title,
    String content
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent()
                );
    }
}