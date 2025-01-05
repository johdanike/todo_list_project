package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;
import org.africa.semicolon.todo_list.Enums.Status;

@Setter
@Getter
public class UpdateTaskResponse {
    private String taskId;
    private String taskName;
    private String taskDescription;
    private Boolean isDone;
    private String message;
    private Status taskStatus;
}
