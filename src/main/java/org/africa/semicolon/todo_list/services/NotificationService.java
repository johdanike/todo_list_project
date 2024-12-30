package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.dtos.requests.AddTaskRequest;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;

public interface NotificationService {
    AddTaskResponse setReminder(AddTaskRequest addTaskRequest);
}
