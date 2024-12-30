package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckOutTaskRequest {
    private String userId;
    private String taskId;
    private String title;
    private Boolean completed;
}
