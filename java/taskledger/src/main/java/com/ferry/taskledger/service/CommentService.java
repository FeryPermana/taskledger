package com.ferry.taskledger.service;

import com.ferry.taskledger.dto.request.CreateCommentRequest;
import com.ferry.taskledger.entity.Comment;
import com.ferry.taskledger.entity.Task;
import com.ferry.taskledger.entity.User;
import com.ferry.taskledger.entity.UserStatus;
import com.ferry.taskledger.repository.CommentRepository;
import com.ferry.taskledger.repository.ProjectMemberRepository;
import com.ferry.taskledger.repository.TaskRepository;
import com.ferry.taskledger.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectMemberRepository projectMemberRepository;

    public CommentService(
        CommentRepository commentRepository,
        TaskRepository taskRepository,
        UserRepository userRepository,
        ProjectMemberRepository projectMemberRepository
    ) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectMemberRepository = projectMemberRepository;
    }

    public Comment createComment(CreateCommentRequest request) {

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> 
                    new NoSuchElementException("Task not found")
                );
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                    new NoSuchElementException("User not found")
                );

        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("User is inactive");
        }

        if (!user.getOrganization().getId().equals(task.getProject().getOrganization().getId())) {
            throw new IllegalArgumentException (
                "User does not belong to project organization"
            );
        }

        if(!projectMemberRepository.existsByProjectIdAndUserId(
            task.getProject().getId(),
            user.getId()
        )) {
            throw new IllegalArgumentException(
                "User is not a member of this project"
            );
        }

        Comment comment = new Comment();

        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(request.getContent());

        return commentRepository.save(comment);
    }

     public List<Comment> getCommentsByTaskId(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new NoSuchElementException("Task not found");
        }

        return commentRepository.findByTaskId(taskId);
    }
}