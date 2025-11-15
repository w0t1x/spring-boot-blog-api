package org.example.service;

import org.example.model.Post;
import org.example.model.Tag;
import org.example.model.User;
import org.example.storage.comment.PostDbStorage;
import org.example.storage.tag.TagDbStorage;
import org.example.storage.user.UserDbStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {
    private final UserDbStorage userDbStorage;
    private final PostDbStorage postDbStorage;
    private final TagDbStorage tagDbStorage;

    public BlogService(UserDbStorage userDbStorage, PostDbStorage postDbStorage, TagDbStorage tagDbStorage) {
        this.userDbStorage = userDbStorage;
        this.postDbStorage = postDbStorage;
        this.tagDbStorage = tagDbStorage;
    }

    @Transactional
    public Post createPostTagFilm(Long authorId, String title, String content, List<String> tagNames){
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
}
