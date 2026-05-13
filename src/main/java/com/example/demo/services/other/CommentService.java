package com.example.demo.services.other;

import com.example.demo.models.other.Comment;
import com.example.demo.models.entities.Student;
import com.example.demo.repositories.other.CommentRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

// Сервіс CommentService містить бізнес-операції для модуля «коментарі студентів».
// Контролери звертаються сюди, щоб не працювати напряму з репозиторіями, mapper-ами та правилами розкладу.
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private RedisTemplate redis;
    // Отримує через Spring залежності commentRepository.
    // Ці сервіси й mapper-и потрібні методам класу для роботи з модулем «коментарі студентів» без ручного створення об’єктів.
    public CommentService(CommentRepository commentRepository, @Qualifier("redisTemplate") RedisTemplate redis) {
        this.commentRepository = commentRepository;
        this.redis = redis;
    }
    // Повертає всі коментарі студентів з репозиторію.
    // Списки, календарі та форми використовують цей метод, коли треба показати весь набір доступних записів.
    public List<Comment> getAll() {
        List<Comment> obj;
        if(redis.opsForValue().get("comments") == null){
            obj = commentRepository.findAll();
            redis.opsForValue().set("comments", obj);
        } else {
            obj = (List<Comment>) redis.opsForValue().get("comments");
        }
        return obj;
    }
    // Знаходить один запис модуля «коментарі студентів» за id.
    // Контролери викликають його перед редагуванням, видаленням або складанням сторінки з деталями.
    public Comment getById(Integer id) {
        Comment obj;
        if(redis.opsForValue().get("commentById"+id) == null){
            obj = commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
            redis.opsForValue().set("commentById"+id, obj);
        } else {
            obj = (Comment) redis.opsForValue().get("commentById"+id);
        }
        return obj;
    }
    // Зберігає новий запис модуля «коментарі студентів».
    // Метод викликається після того, як контролер зібрав сутність з форми або сервіс згенерував її автоматично.
    public Comment create(Comment comment) {
        Comment created = commentRepository.save(comment);
        redis.delete("comments");
        return created;
    }
    // Видаляє записи модуля «коментарі студентів» за умовами: студентом.
    // Операція делегується `commentRepository.deleteAllByStudent`, щоб очистити пов’язані дані після дії користувача.
    public void deleteAllByStudent(Student student){
        commentRepository.deleteAllByStudent(student);
        redis.delete("comments");
    }

    // Оновлює існуючий запис модуля «коментарі студентів».
    // Спочатку знаходить поточну сутність, переносить поля student, teacher, commentText, ratingOfStars і зберігає її назад у репозиторій.
    public Comment update(Integer id, Comment comment) {
        Comment existing = getById(id);
        existing.setStudent(comment.getStudent());
        existing.setTeacher(comment.getTeacher());
        existing.setCommentText(comment.getCommentText());
        existing.setRatingOfStars(comment.getRatingOfStars());
        existing = commentRepository.save(existing);
        redis.delete("commentById"+id);
        redis.delete("comments");
        return existing;
    }
    // Видаляє запис модуля «коментарі студентів» за id.
    // Перед deleteById метод читає сутність, щоб видалення проходило через сервісний шар і падало зрозуміло, якщо id некоректний.
    public void deleteById(Integer id) {
        getById(id);
        commentRepository.deleteById(id);
        redis.delete("commentById"+id);
        redis.delete("comments");
    }
}
