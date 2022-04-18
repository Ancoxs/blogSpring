package com.springblog.blog.controllers;

import com.springblog.blog.models.Post;
import com.springblog.blog.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class BlogController {

    @Autowired
    private PostRepo postRepo;
    @GetMapping("/blog")
    public String blogMain(Model model){
        Iterable<Post> posts = postRepo.findAll();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @GetMapping("/blog/add")
    public String blogAdd(Model model){
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogPost(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model){
        Post post = new Post(title, anons, full_text);
        postRepo.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model){
        if(!postRepo.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepo.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        if(post.isPresent()){
            Post postObj = post.get();
            postObj.setViews(postObj.getViews()+1);
            postRepo.save(postObj);
            res.add(postObj);
        }
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEditing(@PathVariable(value = "id") long id, Model model){
        if(!postRepo.existsById(id)){
            return "redirect:/blog";
        }
        Optional<Post> post = postRepo.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogEditingPost(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model){
        if(!postRepo.existsById(id)){
            return "redirect:/blog";
        }
        Post post = postRepo.findById(id).get();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepo.save(post);
        return "redirect:/blog/{id}";
    }

    @GetMapping("/blog/{id}/remove")
    public String blogPostRemove(@PathVariable(value = "id") long id, Model model){
        if(!postRepo.existsById(id)){
            return "redirect:/blog";
        }
        Post post = postRepo.findById(id).get();
        postRepo.delete(post);
        return "redirect:/blog";
    }
}
