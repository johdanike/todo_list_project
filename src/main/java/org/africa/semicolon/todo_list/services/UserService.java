package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;

import java.util.List;

public interface UserService {
    SignUpResponse signUp(SignUpRequest signUpRequest);
    AddTaskResponse addTask(AddTaskRequest addTaskRequest);
    boolean login(LoginRequest loginRequest);
    CheckOutTaskResponse checkOutTask(CheckOutTaskRequest checkOutTaskRequest);
    UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest);
    AddTaskResponse setReminders(AddTaskRequest addTaskRequest);
    boolean logOut();
    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest);
    DeleteTaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest);
    List<Task> findAll();
}
