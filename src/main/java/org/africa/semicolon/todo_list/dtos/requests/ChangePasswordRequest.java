package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
