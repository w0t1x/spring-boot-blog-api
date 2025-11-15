package org.example.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog_users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(unique = true, length = 150)
    private String email;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    protected User(){}

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    // методы для добавления/удаления двусторонней связи

    public void addPost(Post post){
        posts.add(post);
        post.setAuthor(this);
    }

    public void removePost(Post post){
        posts.remove(post);
        post.setAuthor(null);
    }
}
