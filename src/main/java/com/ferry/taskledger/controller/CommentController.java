package com.ferry.taskledger.controller;

import com.ferry.taskledger.dto.request.CreateCommentRequest;
import com.ferry.taskledger.response.ApiResponse;
import com.ferry.taskledger.dto.response.CommentResponse;
import com.ferry.taskledger.entity.Comment;
import com.ferry.taskledger.mapper.CommentMapper;
import com.ferry.taskledger.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    public CommentController(
            CommentService commentService,
            CommentMapper commentMapper
    ) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    public ApiResponse<CommentResponse> createComment(
            @Valid @RequestBody CreateCommentRequest request
    ) {
        Comment comment = commentService.createComment(request);

        CommentResponse response = commentMapper.toResponse(comment);

        return new ApiResponse<>(
                201,
                "Comment created successfully",
                response
        );
    }

    @GetMapping("/task/{taskId}")
    public ApiResponse<List<CommentResponse>> getCommentsByTaskId(
            @PathVariable Long taskId
    ) {
        List<Comment> comments = commentService.getCommentsByTaskId(taskId);

        List<CommentResponse> response = comments.stream()
                .map(commentMapper::toResponse)
                .toList();

        return new ApiResponse<>(
                200,
                "Comments retrieved successfully",
                response
        );
    }
}