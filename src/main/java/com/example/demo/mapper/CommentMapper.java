package com.example.demo.mapper;

import com.example.demo.dto.CommentDTO;
import com.example.demo.mapper.univMapper.UniversalMapper;
import com.example.demo.models.Comment;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {

    public CommentMapper() {

    }

    public Comment mapCommentDTOToComment(CommentDTO commentDTO) {
        return UniversalMapper.generalMapper(commentDTO, Comment.class);
    }

    public CommentDTO mapCommentToCommentDTO(Comment comment) {
        return UniversalMapper.generalMapper(comment, CommentDTO.class);
    }
}
