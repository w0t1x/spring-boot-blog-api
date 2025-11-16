package org.example.service;

import org.example.model.Post;
import org.example.model.Tag;
import org.example.model.User;
import org.example.storage.comment.CommentDbStorage;
import org.example.storage.post.PostDbStorage;
import org.example.storage.post.TagUsingProection;
import org.example.storage.tag.TagDbStorage;
import org.example.storage.user.UserDbStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BlogService {
    private final UserDbStorage userDbStorage;
    private final PostDbStorage postDbStorage;
    private final TagDbStorage tagDbStorage;
    private final CommentDbStorage commentDbStorage;

    public BlogService(UserDbStorage userDbStorage, PostDbStorage postDbStorage, TagDbStorage tagDbStorage,
                       CommentDbStorage commentDbStorage) {
        this.userDbStorage = userDbStorage;
        this.postDbStorage = postDbStorage;
        this.tagDbStorage = tagDbStorage;
        this.commentDbStorage = commentDbStorage;
    }

    @Transactional
    public Post createPostTagFilm(Long authorId, String title, String content, List<String> tagNames) {
        User author = userDbStorage.findById(authorId)
                .orElseThrow(() -> new NullPointerException("Пользователя с таким id = " + authorId + " не найден"));

        Post post = new Post(title, content);
        post.setAuthor(author);
        author.addPost(post);

        if (tagNames != null) {
            for (String tagName : tagNames) {
                if (tagName == null || tagName.isBlank()) {
                    continue;
                }

                // сначала пробуем найти тег
                Tag tag = tagDbStorage.findName(tagName);

                // если нет — создаём по ИМЕНИ
                if (tag == null) {
                    tag = tagDbStorage.createTag(tagName);
                }

                post.addTag(tag);
            }
        }

        return postDbStorage.createPost(post);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthor(Long authorId) {
        return postDbStorage.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Post> searchPostsByTitle(String text) {
        return postDbStorage.searchByTitle(text);
    }

    @Transactional(readOnly = true)
    public List<Post> getTop3PostsByAuthor(Long authorId) {
        return postDbStorage.findTop3ByAuthor(authorId);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByTag(String tagName) {
        return postDbStorage.findByTagName(tagName);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByAuthorEmail(String email) {
        return postDbStorage.findPostsByAuthorEmail(email);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsBetween(LocalDateTime from, LocalDateTime to) {
        return postDbStorage.findPostsBetween(from, to);
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsWithoutComments() {
        return postDbStorage.findPostsWithoutComments();
    }

    @Transactional(readOnly = true)
    public List<TagUsingProection> getTagUsageStats() {
        return postDbStorage.findTagUsageStats();
    }

    @Transactional
    public void deletePostWithComments(Long postId){
        commentDbStorage.deleteAllByPostId(postId);
        postDbStorage.deleteById(postId);
    }
}
