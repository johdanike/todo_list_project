package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordResponse {
    private String username;
    private boolean isSuccess;
    private String message;
}
