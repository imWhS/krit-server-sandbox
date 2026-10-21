package imwhs.krit_server_sandbox.exception.comment;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class CommentNotFoundException extends BaseException {

    public CommentNotFoundException(Long id) {
        super(
                ErrorType.COMMENT_NOT_FOUND,
                "ID가 " + id + "인 댓글을 찾을 수 없어요.");
    }

    public CommentNotFoundException(String message) {
        super(ErrorType.COMMENT_NOT_FOUND, message);
    }

}
