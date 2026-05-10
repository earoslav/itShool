package com.example.demo.mapper.other;

import com.example.demo.dto.other.CommentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.Comment;
import org.springframework.stereotype.Service;

// Mapper CommentMapper перетворює об’єкти модуля «коментарі студентів» між Entity та DTO.
// Так контролери й HTML-шаблони отримують прості об’єкти без зайвої роботи з JPA-зв’язками.
@Service
public class CommentMapper {

    // Створює порожній CommentMapper для Spring, JPA або UniversalMapper.
    // Такий конструктор потрібен, щоб фреймворк міг створити об’єкт і потім заповнити його поля.
    public CommentMapper() {

    }
    // Перетворює CommentDTO у Comment через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public Comment mapCommentDTOToComment(CommentDTO commentDTO) {
        return UniversalMapper.generalMapper(commentDTO, Comment.class);
    }
    // Перетворює Comment у CommentDTO через UniversalMapper.
    // Це дозволяє контролерам передавати у шаблон DTO або збирати Entity з даних форми без ручного копіювання полів.
    public CommentDTO mapCommentToCommentDTO(Comment comment) {
        return UniversalMapper.generalMapper(comment, CommentDTO.class);
    }
}
