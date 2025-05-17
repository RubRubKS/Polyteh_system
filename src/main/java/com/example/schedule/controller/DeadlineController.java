package com.example.schedule.controller;

import com.example.schedule.model.Deadline;
import com.example.schedule.service.DeadlineService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deadlines")
@AllArgsConstructor
public class DeadlineController {

    private final DeadlineService deadlineService;

    @GetMapping("/{user_id}")
    public List<Deadline> listDeadlines(@PathVariable Long user_id) {
        return deadlineService.findAllDeadlines(user_id);
    }

    @PostMapping("/{user_id}/save_deadline")
    public String saveDeadline(@PathVariable Long user_id, @RequestBody Deadline deadline) {
        return deadlineService.saveDeadline(user_id, deadline);
    }

   @PutMapping("/{user_id}/update_deadline")
   public String updateDeadline(@PathVariable Long user_id, @RequestBody Deadline deadline) {
        return deadlineService.updateDeadline(user_id, deadline);
   }

    @DeleteMapping("/{user_id}/delete/{deadline_id}")
    public String deleteDeadline(@PathVariable Long user_id, @PathVariable Long deadline_id) {
        return deadlineService.deleteDeadline(user_id, deadline_id);
    }
}
