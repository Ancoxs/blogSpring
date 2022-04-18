package com.springblog.blog.repo;

import com.springblog.blog.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepo extends CrudRepository<Post, Long> {
}
