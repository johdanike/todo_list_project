package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteTaskRequest {
    private String taskId;
    private String taskName;
    private String message;
}
