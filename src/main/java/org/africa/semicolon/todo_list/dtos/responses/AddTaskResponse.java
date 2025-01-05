package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.todo_list.Enums.Status;
import org.africa.semicolon.todo_list.data.models.Notification;

@Setter
@Getter
public class AddTaskResponse {
    private String userId;
    private String taskId;
    private String taskName;
    private String taskDescription;
    private Status taskStatus;
    private Notification notification;
    private String message;
}
