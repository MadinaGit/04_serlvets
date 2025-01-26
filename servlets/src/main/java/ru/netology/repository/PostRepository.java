package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    protected final List<Post> posts = new ArrayList<>();
    protected long currentId = 0;

    public synchronized List<Post> all() {
        return Collections.unmodifiableList(new ArrayList<>(posts));
    }

    public synchronized Optional<Post> getById(long id) {
        return posts.stream().filter(post -> post.getId() == id).findFirst();
    }

    public synchronized Post save(Post post) {
        if (post.getId() == 0) {
            currentId++;
            post.setId(currentId);
            posts.add(post);
        } else {
            Optional<Post> existingPost = getById(post.getId());
            if (existingPost.isPresent()) {
                int index = posts.indexOf(existingPost.get());
                posts.set(index, post);
            } else {
                throw new IllegalArgumentException("Post with id " + post.getId() + " does not exist.");
            }
        }
        return post;
    }

    public synchronized void removeById(long id) {
        posts.removeIf(post -> post.getId() == id);
    }

    public void initData() {
        synchronized (this) {
            Post post1 = new Post();
            post1.setContent("Содержимое первого поста");
            save(post1);

            Post post2 = new Post();
            post2.setContent("Содержимое второго поста");
            save(post2);

            Post post3 = new Post();
            post3.setContent("Содержимое третьего поста");
            save(post3);
        }
    }
}
