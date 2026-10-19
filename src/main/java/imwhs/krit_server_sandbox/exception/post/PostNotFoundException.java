package imwhs.krit_server_sandbox.exception.post;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class PostNotFoundException extends BaseException {

    public PostNotFoundException(Long id) {
        super(ErrorType.POST_NOT_FOUND, "ID가 " + id + "인 게시물을 찾을 수 없어요.");
    }

}
