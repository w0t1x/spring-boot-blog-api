package org.example.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreatePostRequestDTO {
    private Long authorId;
    private String title;
    private String content;
    private List<String> tagNames;

    public CreatePostRequestDTO(){}

    public CreatePostRequestDTO(Long authorId, String title, String content, List<String> tagNames) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.tagNames = tagNames;
    }
}
