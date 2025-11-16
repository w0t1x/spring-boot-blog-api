package org.example.storage.tag;

import org.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagStorage extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    List<Tag> findByNameStartingWith(String prefix);
}
