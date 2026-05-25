package com.example.demo.services.other;

import com.example.demo.models.other.Comment;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.other.CommentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// CommentService contains business operations for the "student comments" module.
// Controllers call this service to avoid direct interaction with repositories, mappers, and scheduling rules.
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    // Receives CommentRepository dependency through Spring.
    // These services and mappers are required by the class methods to work with the "student comments" module without manual object creation.
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    // Returns all student comments from the repository.
    // Lists, calendars, and forms use this method when the entire set of available records needs to be shown.
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }
    // Finds a single record in the "student comments" module by ID.
    // Controllers call this before editing, deleting, or assembling a details page.
    public Comment getById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }
    // Saves a new record in the "student comments" module.
    // This method is called after the controller has collected the entity from a form or a service has generated it automatically.
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }
    // Deletes records in the "student comments" module by condition: student.
    // The operation is delegated to `commentRepository.deleteAllByStudent` to clear associated data after a user action.
    public void deleteAllByStudent(Student student){
        commentRepository.deleteAllByStudent(student);
    }

    // Updates an existing record in the "student comments" module.
    // First finds the current entity, transfers student, teacher, commentText, and ratingOfStars fields, then saves it back to the repository.
    public Comment update(Integer id, Comment comment) {
        Comment existing = getById(id);
        existing.setStudent(comment.getStudent());
        existing.setTeacher(comment.getTeacher());
        existing.setCommentText(comment.getCommentText());
        existing.setRatingOfStars(comment.getRatingOfStars());
        return commentRepository.save(existing);
    }
    // Deletes a record in the "student comments" module by ID.
    // Before calling deleteById, the method reads the entity to ensure the deletion passes through the service layer and fails clearly if the ID is incorrect.
    public void deleteById(Integer id) {
        getById(id);
        commentRepository.deleteById(id);
    }
}
