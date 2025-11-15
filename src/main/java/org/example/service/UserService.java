package org.example.service;

import org.example.model.User;
import org.example.storage.user.UserDbStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserDbStorage userDbStorage;

    // внедрение зависимостей через конструктор
    public UserService(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @Transactional
    public User getEmail(String email){
        return userDbStorage.findByEmail(email);
    }

    // Создание пользователя
    @Transactional
    public User createUser(String name, String email) {
        // можно добавить проверки, валидацию и т.п.
        User user = new User(name, email);
        return userDbStorage.save(user);
    }

    // Получение одного пользователя
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return userDbStorage.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Transactional
    public User findUser(User user){
        return userDbStorage.save(user);
    }

    // Получение всех
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDbStorage.findAll();
    }

    // Удаление
    @Transactional
    public void deleteUser(Long id) {
        if (!userDbStorage.existsById(id)) {
            throw new RuntimeException("User not found: " + id);
        }
        userDbStorage.deleteById(id);
    }
}

