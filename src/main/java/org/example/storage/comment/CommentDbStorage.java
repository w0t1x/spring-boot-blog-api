package org.example.storage.comment;

import org.springframework.stereotype.Repository;

@Repository
public class CommentDbStorage {
    private final CommentStorage commentStorage;

    public CommentDbStorage(CommentStorage commentStorage) {
        this.commentStorage = commentStorage;
    }

    public int deleteAllByPostId(Long postId) {
        return commentStorage.deleteAllByPostId(postId);
    }
}
