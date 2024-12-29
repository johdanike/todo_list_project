package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckOutTaskResponse {
    private String taskId;
    private String taskName;
    private boolean completed;
    private String message;
}
