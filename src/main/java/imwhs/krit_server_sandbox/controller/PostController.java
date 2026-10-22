package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.dto.CommentInfo;
import imwhs.krit_server_sandbox.dto.PostRegisterRequest;
import imwhs.krit_server_sandbox.dto.PostInfo;
import imwhs.krit_server_sandbox.dto.PostUpdateRequest;
import imwhs.krit_server_sandbox.service.CommentService;
import imwhs.krit_server_sandbox.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v0/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PostInfo registerPost(@Valid @RequestBody PostRegisterRequest request) {
        return postService.register(request.authorId(), request.title(), request.content());
    }

    @GetMapping("/{id}")
    public PostInfo getPostInfo(@PathVariable Long id) {
        return postService.getInfo(id);
    }

    @GetMapping
    public Page<PostInfo> getAllPostInfos(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getAllInfos(pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest request) { // TODO: 스프링 시큐리티 도입 후 권한 검증 추가
        postService.update(id, request.title(), request.content());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void softDeletePost(
            @PathVariable Long id,
            @RequestParam Long actorId) { // TODO: 스프링 시큐리티 도입 후 세션 인증 정보에서 가져오도록 변경
        postService.softDelete(id, actorId);
    }

    @GetMapping("/{id}/comments")
    public Page<CommentInfo> getAllCommentInfos(
            @PathVariable Long id,
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return commentService.getAllInfosByPostId(id, pageable);
    }

}
