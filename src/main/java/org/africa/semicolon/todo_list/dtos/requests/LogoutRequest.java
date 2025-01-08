package org.africa.semicolon.todo_list.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogoutRequest {
    private String username;
    private String password;
}
