package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddTaskResponse {
    private String userId;
    private String taskId;
    private String taskName;
    private String taskDescription;
    private String taskStatus;
    private String message;
}
