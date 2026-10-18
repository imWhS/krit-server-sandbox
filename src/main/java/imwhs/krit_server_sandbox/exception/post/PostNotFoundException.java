package imwhs.krit_server_sandbox.exception.post;

public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("ID가 " + id + "인 게시물을 찾을 수 없어요.");
    }

}
