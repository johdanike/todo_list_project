package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
}
