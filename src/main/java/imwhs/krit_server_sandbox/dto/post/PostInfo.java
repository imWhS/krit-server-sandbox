package imwhs.krit_server_sandbox.dto.post;

import imwhs.krit_server_sandbox.domain.Post;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record PostInfo(
        Long id,
        String title,
        String content,
        String authorName,
        Set<String> tagNames
) {

    public static PostInfo from(Post post) {
        Set<String> tagNames = new HashSet<>();
        if (!post.getTags().isEmpty()) {
            tagNames = post.getTags().stream()
                    .map(tag -> tag.getTag().getName())
                    .collect(Collectors.toSet());
        }

        PostInfo postInfo = new PostInfo(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getName(),
                tagNames
                );

        return postInfo;
    }

    public static PostInfo from(PostBaseInfo baseInfo, Set<String> tagNames) {
        PostInfo postInfo = new PostInfo(
                baseInfo.id(),
                baseInfo.title(),
                baseInfo.content(),
                baseInfo.authorName(),
                tagNames
        );

        return postInfo;
    }
}