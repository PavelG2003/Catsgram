package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if ((user.getEmail() == null) || (user.getEmail().isBlank())) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (users.values().stream()
                .anyMatch(curUser -> user.getEmail().equals(curUser.getEmail()))) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if ((newUser.getEmail() == null) || (newUser.getPassword() == null) || (newUser.getUsername() == null)) {
                return oldUser;
            }
            String newUserEmail = newUser.getEmail();
            boolean isChangedEmail = !oldUser.getEmail().equals(newUserEmail);
            boolean isNewEmailOverlap = users.values().stream()
                    .anyMatch(user -> user.getEmail().equals(newUserEmail));
            if (isChangedEmail && isNewEmailOverlap) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }

            oldUser.setEmail(newUserEmail);
            oldUser.setUsername(newUser.getUsername());
            oldUser.setPassword(oldUser.getPassword());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    public long getNextId() {
        long curMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++curMaxId;
    }
}
