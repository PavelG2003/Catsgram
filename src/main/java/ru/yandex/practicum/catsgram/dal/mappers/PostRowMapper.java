package ru.yandex.practicum.catsgram.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.catsgram.model.Post;

import java.sql.ResultSet;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class PostRowMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet result, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(result.getLong("id"));
        post.setAuthorId(result.getLong("author_id"));
        post.setDescription(result.getString("description"));
        Timestamp postDate = result.getTimestamp("post_date");
        post.setPostDate(postDate.toInstant());
        return post;
    }
}

