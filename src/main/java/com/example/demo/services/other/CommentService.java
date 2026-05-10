package com.example.demo.services.other;

import com.example.demo.models.other.Comment;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.other.commentRepository;
import java.util.List;
import org.springframework.stereotype.Service;

// Сервіс CommentService містить бізнес-операції для модуля «коментарі студентів».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class CommentService {
    private final commentRepository commentRepository;
    // Отримує через Spring залежності commentRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «коментарі студентів» без ручного створення об’єктів.
    public CommentService(commentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    // Повертає всі коментарі студентів з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }
    // Знаходить один запис модуля «коментарі студентів» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Comment getById(Integer id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }
    // Зберігає новий запис модуля «коментарі студентів».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }
    // Видаляє записи модуля «коментарі студентів» за умовами: студентом.
    // Операція делегується `commentRepository.deleteAllByStudent`, щоб очистити пов’язані дані після дії користувача.
    public void deleteAllByStudent(Student student){
        commentRepository.deleteAllByStudent(student);
    }

    // Оновлює існуючий запис модуля «коментарі студентів».
    // Спочатку знаходить поточну сутність, переносить поля student, teacher, commentText, ratingOfStars і зберігає її назад у репозиторій.
    public Comment update(Integer id, Comment comment) {
        Comment existing = getById(id);
        existing.setStudent(comment.getStudent());
        existing.setTeacher(comment.getTeacher());
        existing.setCommentText(comment.getCommentText());
        existing.setRatingOfStars(comment.getRatingOfStars());
        return commentRepository.save(existing);
    }
    // Видаляє запис модуля «коментарі студентів» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        commentRepository.deleteById(id);
    }
}
