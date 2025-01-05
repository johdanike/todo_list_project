package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.todo_list.Enums.Status;

@Setter
@Getter
public class UpdateTaskRequest {
    private String taskId;
    private String title;
    private String description;
    private String priority;
    private String deadline;
    private Boolean completed;
    private Status taskStatus;
}
