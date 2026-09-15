package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.domain.Post;
import imwhs.krit_server_sandbox.dto.PostRegisterRequest;
import imwhs.krit_server_sandbox.dto.PostResponse;
import imwhs.krit_server_sandbox.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v0/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PostResponse register(@RequestBody PostRegisterRequest request) {
        Post post = postService.register(request.authorId(), request.title(), request.content());
        return PostResponse.from(post);
    }


}
