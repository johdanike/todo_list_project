package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.dtos.requests.LoginRequest;
import org.africa.semicolon.todo_list.dtos.requests.LogoutRequest;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.LogOutResponse;
import org.africa.semicolon.todo_list.dtos.responses.LoginResponse;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;

public interface AuthenticationService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    LoginResponse login(LoginRequest loginRequest);
    LogOutResponse logout(LogoutRequest logoutRequest);
}
