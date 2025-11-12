package com.example.simpleblog.controller;

import com.example.simpleblog.model.Post;
import com.example.simpleblog.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PostController {

    private final PostRepository repo;

    // Constructor Injection for repository
    public PostController(PostRepository repo) {
        this.repo = repo;
    }

    // Home Page - Displays all posts
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("posts", repo.findAll());
        return "index";
    }

    // View Single Post Page
    @GetMapping("/view/{id}")
    public String viewPost(@PathVariable Long id, Model model) {
        Post post = repo.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "view";
    }

    // Create Post Page
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    // Save Post Handler (Fix for your 404)
    @PostMapping("/save")
    public String savePost(@ModelAttribute Post post) {
        repo.save(post);
        return "redirect:/";
    }

    // Edit Post Page
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Post post = repo.findById(id).orElse(null);
        model.addAttribute("post", post);
        return "edit";
    }

    // Update Post Handler
    @PostMapping("/update/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        Post existing = repo.findById(id).orElse(null);
        if (existing != null) {
            existing.setTitle(post.getTitle());
            existing.setAuthor(post.getAuthor());
            existing.setContent(post.getContent());
            repo.save(existing);
        }
        return "redirect:/";
    }

    // Delete Post Handler
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/";
    }

    // Search Functionality
    @GetMapping("/search")
    public String searchPosts(@RequestParam String keyword, Model model) {
        List<Post> posts = repo.findAll().stream()
                .filter(p -> p.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        p.getContent().toLowerCase().contains(keyword.toLowerCase()) ||
                        p.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        model.addAttribute("posts", posts);
        model.addAttribute("searchQuery", keyword);
        return "index";
    }
}
