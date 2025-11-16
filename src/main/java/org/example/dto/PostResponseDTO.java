package org.example.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostResponseDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private AuthorDto author;
    private List<TagDto> tags;
    private int commentsCount;

    public PostResponseDTO() {
    }

    public PostResponseDTO(Long id,
                        String title,
                        String content,
                        LocalDateTime createdAt,
                        AuthorDto author,
                        List<TagDto> tags,
                        int commentsCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.author = author;
        this.tags = tags;
        this.commentsCount = commentsCount;
    }
}
