package imwhs.krit_server_sandbox.exception.post;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostNotFoundException extends RuntimeException {

    private final String errorCode = "POST_NOT_FOUND";

    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public PostNotFoundException(Long id) {
        super("ID가 " + id + "인 게시물을 찾을 수 없어요.");
    }

}
