package ru.yandex.practicum.catsgram.model;

import lombok.*;

import java.time.Instant;

@Data
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "id" })
public class Post implements Comparable<Post>{
    private Long id;
    private long authorId;
    private  String description;
    private Instant postDate;

    @Override
    public int compareTo(Post post) {
        return this.id.compareTo(post.getId());
    }
}
