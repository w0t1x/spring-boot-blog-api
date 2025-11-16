package org.example.controller;

import org.example.dto.CreatePostRequestDTO;
import org.example.dto.PostResponseDTO;
import org.example.dto.mapper.PostMapper;
import org.example.model.Post;
import org.example.service.BlogService;
import org.example.storage.post.TagUsingProection;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public PostResponseDTO createPost(@RequestBody CreatePostRequestDTO request) {
        Post post = blogService.createPostTagFilm(request.getAuthorId(),
                request.getTitle(), request.getContent(), request.getTagNames());
        return PostMapper.toDto(post);
    }

    // все посты автора
    @GetMapping("/posts/author/{authorId}")
    public List<PostResponseDTO> getPostsByAuthor(@PathVariable Long authorId) {
        return blogService.getPostsByAuthor(authorId).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // поиск постов по части заголовка
    @GetMapping("/posts/search")
    public List<PostResponseDTO> searchPosts(@RequestParam String text) {
        return blogService.searchPostsByTitle(text).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // последние 3 поста автора
    @GetMapping("/posts/author/{authorId}/top3")
    public List<PostResponseDTO> getTop3PostsByAuthor(@PathVariable Long authorId) {
        return blogService.getTop3PostsByAuthor(authorId).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // посты по тегу
    @GetMapping("/posts/by-tag")
    public List<PostResponseDTO> getPostsByTag(@RequestParam String tagName) {
        return blogService.getPostsByTag(tagName).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // посты по email автора
    @GetMapping("/posts/by-author-email")
    public List<PostResponseDTO> getPostsByAuthorEmail(@RequestParam String email) {
        return blogService.getPostsByAuthorEmail(email).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // посты между датами (пример)
    @GetMapping("/posts/between")
    public List<PostResponseDTO> getPostsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return blogService.getPostsBetween(from, to).stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // посты без комментариев
    @GetMapping("/posts/without-comments")
    public List<PostResponseDTO> getPostsWithoutComments() {
        return blogService.getPostsWithoutComments().stream()
                .map(PostMapper::toDto)
                .toList();
    }

    // статистика по тегам
    @GetMapping("/stats/tags")
    public List<TagUsingProection> getTagStats() {
        return blogService.getTagUsageStats();
    }

    // удаление поста вместе с комментариями
    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable Long id) {
        blogService.deletePostWithComments(id);
    }
}
