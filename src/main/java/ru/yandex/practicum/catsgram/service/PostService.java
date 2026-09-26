package ru.yandex.practicum.catsgram.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.dal.PostRepository;
import ru.yandex.practicum.catsgram.dto.post.NewPostRequest;
import ru.yandex.practicum.catsgram.dto.post.UpdatePostRequest;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.mapper.PostMapper;
import ru.yandex.practicum.catsgram.model.Post;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;

    public List<Post> findAll(int from, int size, SortOrder sort) {
        return postRepository.findAll(sort, from, size);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пост с id: " + id + " не найден"));
    }

    public Post create(NewPostRequest request) {
        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if (userService.getUserById(request.getAuthorId()) == null) {
            throw new ConditionsNotMetException("«Автор с id = " + request.getAuthorId() + " не найден»");
        }

        Post post = PostMapper.mapToPost(request);
        post = postRepository.save(post);
        return post;
    }

    public Post update(long postId, UpdatePostRequest request) {
        Post updatedPost = postRepository.findById(postId)
                .map(post -> PostMapper.updatePostFields(post, request))
                .orElseThrow(() -> new NotFoundException("Пост с id: " + postId + " не найден"));
        updatedPost = postRepository.update(updatedPost);
        return updatedPost;
    }
}