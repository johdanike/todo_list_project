package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.todo_list.data.models.Notification;

@Setter
@Getter
public class AddTaskRequest {
    private String title;
    private String description;
    private String priority;
    private Notification notification;
    private String deadline;
    private String userId;
}
