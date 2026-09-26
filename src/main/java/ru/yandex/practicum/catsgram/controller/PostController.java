package ru.yandex.practicum.catsgram.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.dto.post.FindAllPostRequest;
import ru.yandex.practicum.catsgram.dto.post.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.post.UpdatePostRequest;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping
    public List<Post> findAll(FindAllPostRequest request) {
        SortOrder sortParam = SortOrder.from(request.getSort());
        return postService.findAll(
                request.getFrom(),
                request.getSize(),
                sortParam
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody NewPostRequest request) {
        return postService.create(request);
    }

    @PutMapping({"/postId"})
    public Post update(@PathVariable("postId") long postId, @RequestBody UpdatePostRequest request) {
        return postService.update(postId, request);
    }
}