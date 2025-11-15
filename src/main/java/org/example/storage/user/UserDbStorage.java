package org.example.storage.user;

import org.example.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDbStorage {
    private final UserStorage userStorage;

    public UserDbStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public User findByEmail(String email) {
        return userStorage.findByEmail(email);
    }


    public User save(User user) {
        return userStorage.save(user);
    }


    public Optional<User> findById(Long id) {
        return userStorage.findById(id);
    }


    public List<User> findAll() {
        return userStorage.findAll();
    }


    public boolean existsById(Long id) {
        return userStorage.existsById(id);
    }


    public void deleteById(Long id) {
        userStorage.deleteById(id);
    }
}
