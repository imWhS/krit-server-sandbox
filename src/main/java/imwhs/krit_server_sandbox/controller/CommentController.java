package imwhs.krit_server_sandbox.controller;

import imwhs.krit_server_sandbox.dto.comment.CommentInfo;
import imwhs.krit_server_sandbox.dto.comment.CommentRegisterRequest;
import imwhs.krit_server_sandbox.dto.comment.CommentUpdateRequest;
import imwhs.krit_server_sandbox.security.annotation.AuthenticatedKritAccountId;
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
    public CommentInfo registerComment(
            @Valid @RequestBody CommentRegisterRequest request,
            @AuthenticatedKritAccountId Long authorId) {
        return commentService.register(authorId, request.postId(), request.body());
    }

    @GetMapping("/{id}")
    public CommentInfo getCommentInfo(@PathVariable Long id) {
        return commentService.getInfo(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request,
            @AuthenticatedKritAccountId Long actorId) {
        commentService.updateBody(actorId, id, request.body());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void softDeleteComment(
            @PathVariable Long id,
            @AuthenticatedKritAccountId Long actorId) {
        commentService.softDelete(id, actorId);
    }

}
