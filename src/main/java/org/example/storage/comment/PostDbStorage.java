package org.example.storage.comment;

import org.example.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostDbStorage{
    private final PostStorage postStorage;

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
}
