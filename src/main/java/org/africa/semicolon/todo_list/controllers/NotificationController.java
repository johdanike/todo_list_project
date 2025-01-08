package org.africa.semicolon.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import org.africa.semicolon.todo_list.dtos.requests.AddTaskRequest;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;
import org.africa.semicolon.todo_list.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/setNotification")
    public ResponseEntity<String> setNotification(@RequestBody AddTaskRequest addTaskRequest) {
        try {
            AddTaskResponse addTaskResponse = notificationService.setReminder(addTaskRequest);
            return ResponseEntity.ok(addTaskResponse.toString());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
