package imwhs.krit_server_sandbox.dto.post;

import java.util.List;

public record PostInfoCursorResponse(
        List<PostInfo> content,
        boolean hasNext,
        Long nextCursor
) {}
