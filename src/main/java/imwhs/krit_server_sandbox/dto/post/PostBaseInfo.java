package imwhs.krit_server_sandbox.dto.post;

public record PostBaseInfo(
        Long id,
        String title,
        String content,
        String authorName
) {}
