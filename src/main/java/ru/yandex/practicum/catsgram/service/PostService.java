package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class PostService {
    private final UserService userService;
    private final Map<Long, Post> posts = new HashMap<>();

    public PostService(UserService userService) {
        this.userService = userService;
    }

    public Collection<Post> findAll(int from, int size, String sort) {
        List<Post> allPosts = new ArrayList<>(posts.values());

        if(SortOrder.from(sort) == SortOrder.ASCENDING) {
            allPosts.sort(Comparator.naturalOrder());
        }else if(SortOrder.from(sort) == SortOrder.DESCENDING) {
            allPosts.sort(Comparator.reverseOrder());
        }else {
            throw new ParameterNotValidException(sort,
                    "Некорректный тип сортировки.");
        }

        return allPosts.stream().
                filter(post -> post.getId() > from).
                limit(size).
                toList();
    }

    public Post create(Post post) {
        if (post.getDescription() == null || post.getDescription().isBlank()) {
            throw new ConditionsNotMetException("Описание не может быть пустым");
        }
        if(userService.findUserById(post.getAuthorId()) == null){
            throw new ConditionsNotMetException("«Автор с id = " + post.getAuthorId() + " не найден»");
        }
        post.setId(getNextId());
        post.setPostDate(Instant.now());
        posts.put(post.getId(), post);
        return post;
    }

    public Post update(Post newPost) {
        if (newPost.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        if (posts.containsKey(newPost.getId())) {
            Post oldPost = posts.get(newPost.getId());
            if (newPost.getDescription() == null || newPost.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание не может быть пустым");
            }
            oldPost.setDescription(newPost.getDescription());
            return oldPost;
        }
        throw new NotFoundException("Пост с id = " + newPost.getId() + " не найден");
    }

    public Post findPostById(Long id){
        Post post = posts.get(id);
        if(post != null) {
            return post;
        }
        throw new NotFoundException("Пост с таким id не найден!");
    }

    private long getNextId() {
        long currentMaxId = posts.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}