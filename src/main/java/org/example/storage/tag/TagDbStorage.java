package org.example.storage.tag;

import org.example.model.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagDbStorage {
    private final TagStorage tagStorage;

    public TagDbStorage(TagStorage tagStorage) {
        this.tagStorage = tagStorage;
    }

    public Tag findName(String name){
        return tagStorage.findByName(name);
    }

    public Tag createTag(String name){
        Tag tag = new Tag(name);
        return tagStorage.save(tag);
    }

    public List<Tag> findByNameStartingWith(String prefix){
        return tagStorage.findByNameStartingWith(prefix);
    }
}
