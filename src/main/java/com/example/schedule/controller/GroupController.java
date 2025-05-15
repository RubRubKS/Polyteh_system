package com.example.schedule.controller;

import com.example.schedule.model.ErrorResponse;
import com.example.schedule.model.Group;
import com.example.schedule.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups/")
@AllArgsConstructor
public class GroupController {

    GroupService groupService;

    @GetMapping
    public ResponseEntity<?> getAllGroups() {

        try {
            List<Group> groups = groupService.getAllGroups();

            if (groups == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Группы не найдены в базе данных."));
            }

            return ResponseEntity.ok(groups);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    @PostMapping("save_group")
    public ResponseEntity<?> saveGroup(@RequestBody Group group) {

        try {
            String response = groupService.saveGroup(group);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Не удалось сохранить группу."));
            }

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

    @DeleteMapping("delete_group/{group_number}")
    public ResponseEntity<?> deleteGroup(@PathVariable String group_number) {

        try {
            String response = groupService.deleteGroupByGroupNumber(group_number);

            if (response == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Не удалось удалить группу."));
            }

            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(e.getMessage()));
        }

    }

}
