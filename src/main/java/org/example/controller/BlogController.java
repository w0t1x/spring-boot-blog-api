package org.example.controller;

import org.example.model.Post;
import org.example.service.BlogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {
    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @PostMapping
    public Post createPost(@RequestParam Long authorId,
                           @RequestParam String title,
                           @RequestParam String content,
                           @RequestParam List<String> tagNames){
        return blogService.createPostTagFilm(authorId, title, content, tagNames);
    }

}
