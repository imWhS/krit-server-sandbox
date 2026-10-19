package imwhs.krit_server_sandbox.exception.post;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {

    private final String code = "POST_NOT_FOUND";

    public PostNotFoundException(Long id) {
        super("ID가 " + id + "인 게시물을 찾을 수 없어요.");
    }

}
