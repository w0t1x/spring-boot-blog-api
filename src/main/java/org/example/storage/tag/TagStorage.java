package org.example.storage.tag;

import org.example.dto.TagUsageDTO;
import org.example.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagStorage extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    List<Tag> findByNameStartingWith(String prefix);

    @Query("""
            select new org.example.dto.TagUsageDTO(t.name, count(p))
            from Post p
            join p.tags t
            group by t.name
            order by count(p) desc
            """)
    List<TagUsageDTO> findTagUsageStats();
}
