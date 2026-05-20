package com.example.demo.mapper.other;

import com.example.demo.dto.other.CommentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.other.Comment;
import org.springframework.stereotype.Service;

// CommentMapper converts "student comments" module objects between Entity and DTO.
// This way, controllers and HTML templates receive simple objects without extra work with JPA relationships.
@Service
public class CommentMapper {

    // Creates an empty CommentMapper for Spring, JPA, or UniversalMapper.
    // Such a constructor is needed so the framework can create the object and then fill its fields.
    public CommentMapper() {

    }
    // Converts CommentDTO to Comment using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public Comment mapCommentDTOToComment(CommentDTO commentDTO) {
        return UniversalMapper.generalMapper(commentDTO, Comment.class);
    }
    // Converts Comment to CommentDTO using UniversalMapper.
    // This allows controllers to pass a DTO to the template or assemble an Entity from form data without manual field copying.
    public CommentDTO mapCommentToCommentDTO(Comment comment) {
        return UniversalMapper.generalMapper(comment, CommentDTO.class);
    }
}
