package org.example.service;

import org.example.dto.CreatePostRequestDTO;
import org.example.dto.PostResponseDTO;
import org.example.dto.mapper.PostMapper;
import org.example.model.Post;
import org.example.model.Tag;
import org.example.model.User;
import org.example.storage.comment.CommentDbStorage;
import org.example.storage.post.PostDbStorage;
import org.example.storage.post.TagUsingProection;
import org.example.storage.tag.TagDbStorage;
import org.example.storage.user.UserDbStorage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public PostResponseDTO createPostTagFilm(CreatePostRequestDTO request) {
        User author = userDbStorage.findById(request.getAuthorId())
                .orElseThrow(() -> new NullPointerException("Пользователя с таким id = " + request.getAuthorId() + " не найден"));

        Post post = new Post(request.getTitle(), request.getContent());
        post.setAuthor(author);
        author.addPost(post);

        if (request.getTagNames() != null) {
            for (String tagName : request.getTagNames()) {
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

        Post create = postDbStorage.createPost(post);
        return PostMapper.toDto(create);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getPostsByAuthor(Long authorId, Pageable pageable) {
        return postDbStorage.findByAuthorId(authorId, pageable).map(PostMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> searchPostsByTitle(String text, Pageable pageable) {
        return postDbStorage.searchByTitle(text, pageable).map(PostMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDTO> getTop3PostsByAuthor(Long authorId, Pageable pageable) {
        return postDbStorage.findTop3ByAuthor(authorId, pageable).map(PostMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByTag(String tagName) {
        return postDbStorage.findByTagName(tagName).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsByAuthorEmail(String email) {
        return postDbStorage.findPostsByAuthorEmail(email).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsBetween(LocalDateTime from, LocalDateTime to) {
        return postDbStorage.findPostsBetween(from, to).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostResponseDTO> getPostsWithoutComments() {
        return postDbStorage.findPostsWithoutComments().stream()
                .map(PostMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TagUsingProection> getTagUsageStats() {
        return postDbStorage.findTagUsageStats();
    }

    @Transactional
    public void deletePostWithComments(Long postId) {
        commentDbStorage.deleteAllByPostId(postId);
        postDbStorage.deleteById(postId);
    }
}
