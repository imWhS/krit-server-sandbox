package imwhs.krit_server_sandbox.exception.tag;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class TagDuplicateException extends BaseException {

    public TagDuplicateException(String name) {
        super(ErrorType.TAG_DUPLICATE, "이미 " + name + " 이름을 가진 태그가 있어요.");
    }

}
