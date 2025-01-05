package org.africa.semicolon.todo_list.data.models;

import lombok.Data;
import org.africa.semicolon.todo_list.Enums.Status;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document
public class Task {
    @Id
    private String id;
    @DBRef
    private String userId;
    private String title;
    private String description;
    private String priority;
    private LocalDateTime createdAt;
    private Status status;
    private String deadline;
    private Boolean completed;
    private Notification notification;
}
