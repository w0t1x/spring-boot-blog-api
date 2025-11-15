package org.example.storage.user;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStorage extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
