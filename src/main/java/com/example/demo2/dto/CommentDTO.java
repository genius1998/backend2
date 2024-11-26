package com.example.demo2.dto;

import com.example.demo2.entity.comment2;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 직렬화하지 않음
public class CommentDTO {
    private Long id;
    private String content;
    private String author;
    private String timestamp;

    public CommentDTO(comment2 comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getAuthor();
        this.timestamp = comment.getTimestamp();
    }
}
