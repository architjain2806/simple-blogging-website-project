package com.example.simpleblog.repository;

import com.example.simpleblog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
