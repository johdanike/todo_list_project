package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    AddTaskResponse addTask(AddTaskRequest addTaskRequest);
    boolean login(LoginRequest loginRequest);
    CheckOutTaskResponse checkOutTask(CheckOutTaskRequest checkOutTaskRequest);
    UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest);
    AddTaskResponse getReminders(AddTaskRequest addTaskRequest);
}
