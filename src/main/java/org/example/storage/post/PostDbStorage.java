package org.example.storage.post;

import org.example.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostDbStorage{
    private final org.example.storage.post.PostStorage postStorage;

    public PostDbStorage(PostStorage postStorage) {
        this.postStorage = postStorage;
    }

    public Post createPost(Post post){
        return postStorage.save(post);
    }

    public void deleteById(Long id) {
        postStorage.deleteById(id);
    }

    public Page<Post> findByAuthorId(Long authorId, Pageable pageable) {
        return postStorage.findByAuthorId(authorId, pageable);
    }

    public Page<Post> searchByTitle(String text, Pageable pageable) {
        return postStorage.findByTitleContainingIgnoreCase(text, pageable);
    }

    public Page<Post> findTop3ByAuthor(Long authorId, Pageable pageable) {
        return postStorage.findTop3ByAuthorIdOrderByCreatedAtDesc(authorId, pageable);
    }

    public List<Post> findByTagName(String tagName) {
        return postStorage.findByTagsName(tagName);
    }

    public List<Post> findPostsByAuthorEmail(String email) {
        return postStorage.findByAuthorEmail(email);
    }

    public List<Post> findPostsBetween(LocalDateTime from, LocalDateTime to) {
        return postStorage.findPostsBetween(from, to);
    }

    public List<Post> findPostsWithoutComments() {
        return postStorage.findPostsWithoutComments();
    }

    public List<TagUsingProection> findTagUsageStats() {
        return postStorage.findTagUsageStats();
    }
}
