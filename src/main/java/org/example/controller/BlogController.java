package org.example.controller;

import org.example.dto.CreatePostRequestDTO;
import org.example.dto.PostResponseDTO;
import org.example.service.BlogService;
import org.example.storage.post.TagUsingProection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        return blogService.createPostTagFilm(request);
    }

    // все посты автора
    @GetMapping("/posts/author/{authorId}")
    public Page<PostResponseDTO> getPostsByAuthor(@PathVariable Long authorId,
                                                  @PageableDefault(sort = "createdAt",
                                                          direction = Sort.Direction.DESC) Pageable pageable) {
        return blogService.getPostsByAuthor(authorId, pageable);
    }

    // поиск постов по части заголовка
    @GetMapping("/posts/search")
    public Page<PostResponseDTO> searchPosts(@RequestParam String text,
                                             @PageableDefault(sort = "createdAt",
                                                     direction = Sort.Direction.DESC) Pageable pageable) {
        return blogService.searchPostsByTitle(text, pageable);
    }

    // последние 3 поста автора
    @GetMapping("/posts/author/{authorId}/top3")
    public Page<PostResponseDTO> getTop3PostsByAuthor(@PathVariable Long authorId,
                                                      @PageableDefault(sort = "createdAt",
                                                              direction = Sort.Direction.DESC) Pageable pageable) {
        return blogService.getTop3PostsByAuthor(authorId, pageable);
    }

    // посты по тегу
    @GetMapping("/posts/by-tag")
    public List<PostResponseDTO> getPostsByTag(@RequestBody @RequestParam String tagName) {
        return blogService.getPostsByTag(tagName);
    }

    // посты по email автора
    @GetMapping("/posts/by-author-email")
    public List<PostResponseDTO> getPostsByAuthorEmail(@RequestParam String email) {
        return blogService.getPostsByAuthorEmail(email);
    }

    // посты между датами (пример)
    @GetMapping("/posts/between")
    public List<PostResponseDTO> getPostsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return blogService.getPostsBetween(from, to);
    }

    // посты без комментариев
    @GetMapping("/posts/without-comments")
    public List<PostResponseDTO> getPostsWithoutComments() {
        return blogService.getPostsWithoutComments();
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
