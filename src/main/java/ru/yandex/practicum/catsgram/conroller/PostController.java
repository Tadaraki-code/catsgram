package ru.yandex.practicum.catsgram.conroller;



import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;
import ru.yandex.practicum.catsgram.service.SortOrder;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue =  "10") int size,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "desc") String sort) {
        if (size <= 0) {
            throw new ParameterNotValidException(String.valueOf(size),
                    "Некорректный размер выборки. Размер должен быть больше нуля" );
        }
        if (from < 0) {
            throw new ParameterNotValidException(String.valueOf(from),
                    "Некорректная метка начала выборки. Метка не может быть меньше нуля");
        }
        if(SortOrder.from(sort) == null) {
            throw new ParameterNotValidException(sort,
                    "Некорректный тип сортировки.");
        }
        return postService.findAll(from, size, sort);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @GetMapping("/{id}")
    public Post findPostById(@PathVariable("id") Long id){
            return postService.findPostById(id);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }
}