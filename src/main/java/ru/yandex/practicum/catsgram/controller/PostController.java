package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public Optional<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        if (!sort.equals("ascending") && !sort.equals("asc") && !sort.equals("descending") && !sort.equals("desc")) {
            throw new ParameterNotValidException(
                    sort,
                    "Неккоректный параметр сортировки, допустимые варианты: ascending, asc, descending, desc"
            );
        }
        if (size <= 0) {
            throw new ParameterNotValidException(
                    String.valueOf(size),
                    "Некорректный размер выборки. Размер должен быть больше нуля"
            );
        }
        if (from < 0) {
            throw new ParameterNotValidException(
                    String.valueOf(from),
                    "Некорректный значение начала выборки. Начало выборки должно быть не меньше нуля"
            );
        }
        SortOrder sortParam = SortOrder.from(sort);
        return postService.findAll(from, size, sortParam);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}