package org.example.storage.comment;

import org.example.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentStorage extends JpaRepository<Comment, Long> {

    @Modifying
    @Transactional
    @Query("""
            delete from Comment c where c.post.id = :postId  
            """)
    int deleteAllByPostId(@Param("postId") Long postId);
}
