package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.dto.comment.CommentInfo;
import imwhs.krit_server_sandbox.dto.comment.CommentRegisterRequest;
import imwhs.krit_server_sandbox.dto.comment.CommentUpdateRequest;
import imwhs.krit_server_sandbox.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v0/comments")
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentInfo registerComment(@Valid @RequestBody CommentRegisterRequest request) {
        return commentService.register(request.authorId(), request.postId(), request.body());
    }

    @GetMapping("/{id}")
    public CommentInfo getCommentInfo(@PathVariable Long id) {
        return commentService.getInfo(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request) { // TODO: 스프링 시큐리티 도입 후 권한 검증 추가
        commentService.updateBody(id, request.body());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void softDeleteComment(
            @PathVariable Long id,
            @RequestParam Long actorId) { // TODO: 스프링 시큐리티 도입 후 세션 인증 정보에서 가져오도록 변경
        commentService.softDelete(id, actorId);
    }

}
