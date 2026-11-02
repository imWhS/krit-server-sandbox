package imwhs.krit_server_sandbox.exception.post;

import imwhs.krit_server_sandbox.common.error.ErrorType;
import imwhs.krit_server_sandbox.exception.BaseException;

public class PostVersionConflictException extends BaseException {

    public PostVersionConflictException() {
        super(
                ErrorType.OPTIMISTIC_LOCK_CONFLICT,
                "요청을 처리하던 중에 다른 요청에 의해 정보가 수정됐어요. 수정된 정보를 확인하신 후 다시 시도해주세요.");
    }

}
