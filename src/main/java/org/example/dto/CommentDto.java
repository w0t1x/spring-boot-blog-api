package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private LocalDateTime createdAt;

    public CommentDto() {
    }

    public CommentDto(Long id, String text, LocalDateTime createdAt) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
    }
}
