package com.example.demo.services;

import com.example.demo.models.Comment;
import com.example.demo.models.Student;
import com.example.demo.repositories.commentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final commentRepository commentRepository;

    public CommentService(commentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment getById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }
    public void deleteAllByStudent(Student student){
        commentRepository.deleteAllByStudent(student);
    }

    public Comment update(Integer id, Comment comment) {
        Comment existing = getById(id);
        existing.setStudent(comment.getStudent());
        existing.setTeacher(comment.getTeacher());
        existing.setCommentText(comment.getCommentText());
        existing.setRatingOfStars(comment.getRatingOfStars());
        return commentRepository.save(existing);
    }

    public void deleteById(Integer id) {
        getById(id);
        commentRepository.deleteById(id);
    }
}
