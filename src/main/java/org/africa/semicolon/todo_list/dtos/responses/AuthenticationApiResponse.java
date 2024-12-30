package org.africa.semicolon.todo_list.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationApiResponse {
    Boolean isSuccess;
    Object signUpResponse;
}
