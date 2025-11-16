package org.example.storage.post;

import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PostDbStorage{
    private final org.example.storage.post.PostStorage postStorage;

    public PostDbStorage(PostStorage postStorage) {
        this.postStorage = postStorage;
    }

    public List<Post> authorId(Long id){
        return postStorage.findByAuthorId(id);
    }

    public List<Post> tagName(String tagName){
        return postStorage.findByTagsName(tagName);
    }

    public Post createPost(Post post){
        return postStorage.save(post);
    }

    public void deleteById(Long id) {
        postStorage.deleteById(id);
    }

    public List<Post> findByAuthorId(Long authorId) {
        return postStorage.findByAuthorId(authorId);
    }

    public List<Post> searchByTitle(String text) {
        return postStorage.findByTitleContainingIgnoreCase(text);
    }

    public List<Post> findTop3ByAuthor(Long authorId) {
        return postStorage.findTop3ByAuthorIdOrderByCreatedAtDesc(authorId);
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
