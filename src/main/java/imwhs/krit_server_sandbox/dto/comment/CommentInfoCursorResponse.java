package imwhs.krit_server_sandbox.dto.comment;

import java.util.List;

public record CommentInfoCursorResponse(
        List<CommentInfo> content,
        boolean hasNext,
        Long nextCursor
) {
}
