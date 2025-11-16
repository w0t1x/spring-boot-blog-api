package org.example.storage.post;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public interface PostSummaryProjection{
    Long getId();
    String getTitle();
    LocalDateTime getCreatedAt();

    AuthorInfo getAuthor();

    interface AuthorInfo {
        Long getId();
        String getName();
    }
}
