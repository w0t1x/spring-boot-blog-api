package org.example.storage.post;

import org.example.model.Post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostStorage extends JpaRepository<Post, Long> {

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    List<Post> findByTagsName(String tagName);

    Page<Post> findByTitleContainingIgnoreCase(String text, Pageable pageable);

    Page<Post> findTop3ByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    @Query("""
            select p from Post p
            where p.author.email = :email
            """)
    List<Post> findByAuthorEmail(@Param("email") String email);

    @Query("""
            select p from Post p
            where p.createdAt between :from and :to
            order by p.createdAt desc
            """)
    List<Post> findPostsBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("""
            select p from Post p
            left join p.comments c
            where c is null
            """)
    List<Post> findPostsWithoutComments();

    @Query("""
            select t.name as tagName, count(p) as postCount
            from Post p
            join p.tags t
            group by t.name
            order by postCount desc
            """)
    List<TagUsingProection> findTagUsageStats();
}
