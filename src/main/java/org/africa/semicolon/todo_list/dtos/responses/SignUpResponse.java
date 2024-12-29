package org.africa.semicolon.todo_list.dtos.responses;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignUpResponse {
    private String message;
    private String username;
    private String id;
}
