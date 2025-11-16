package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY) // желательно LAZY, чтобы не тянуть автора всегда
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Один пост - много комментариев
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags = new ArrayList<>();

    protected Post(){}

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setPost(this);
    }
    public void deleteComment(Comment comment){
        comments.remove(comment);
        comment.setPost(null);
    }

    public void addTag(Tag tag){
        tags.add(tag);
        tag.getPosts().add(this);
    }
    public void deleteTag(Tag tag){
        tags.remove(tag);
        tag.getPosts().add(null);
    }
}
