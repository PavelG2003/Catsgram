package ru.yandex.practicum.catsgram.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.catsgram.enums.SortOrder;
import ru.yandex.practicum.catsgram.model.Post;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class PostRepository extends BaseRepository<Post> {
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM posts WHERE id = ?";
    private static final String FIND_ALL_ASC_QUERY = "SELECT * FROM posts ORDER BY post_date ASC LIMIT ? OFFSET ?";
    private static final String FIND_ALL_DESC_QUERY = "SELECT * FROM posts ORDER BY post_date DESC LIMIT ? OFFSET ?";
    private static final String INSERT_QUERY = "INSERT INTO posts(author_id, description, post_date)" +
            "VALUES (?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE posts SET author_id = ?, description = ?, post_date = ?" +
            " WHERE id = ?";

    public PostRepository(JdbcTemplate jdbc, RowMapper<Post> mapper) {
        super(jdbc, mapper);
    }

    public Optional<Post> findById(long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    public List<Post> findAll(SortOrder sort, int from, int size) {
        String query = (sort == SortOrder.ASCENDING) ? FIND_ALL_ASC_QUERY : FIND_ALL_DESC_QUERY;
        return findMany(query, size, from);
    }

    public Post save(Post post) {
        long id = insert(
                INSERT_QUERY,
                post.getAuthorId(),
                post.getDescription(),
                Timestamp.from(post.getPostDate())
        );
        post.setId(id);
        return post;
    }

    public Post update(Post post) {
        update(
                UPDATE_QUERY,
                post.getAuthorId(),
                post.getDescription(),
                post.getPostDate(),
                post.getId()
        );
        return post;
    }
}
