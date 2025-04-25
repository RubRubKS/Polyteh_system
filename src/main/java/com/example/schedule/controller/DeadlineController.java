package com.example.schedule.controller;

import com.example.schedule.repository.DeadlineRepository;
import com.example.schedule.model.Deadline;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/deadlines")
public class DeadlineController {

    private final DeadlineRepository deadlineRepository;

    public DeadlineController(DeadlineRepository deadlineRepository) {
        this.deadlineRepository = deadlineRepository;
    }

    @GetMapping
    public String listDeadlines(Model model) {
        model.addAttribute("deadlines", deadlineRepository.findAll());
        return "deadlines/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("deadline", new Deadline());
        return "deadlines/form";
    }

    @PostMapping("/save")
    public String saveDeadline(@ModelAttribute Deadline deadline) {
        deadlineRepository.save(deadline);
        return "redirect:/deadlines";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("deadline", deadlineRepository.findById(id).orElseThrow());
        return "deadlines/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteDeadline(@PathVariable Long id) {
        deadlineRepository.deleteById(id);
        return "redirect:/deadlines";
    }
}
