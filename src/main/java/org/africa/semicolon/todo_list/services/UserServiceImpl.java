package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;
import org.africa.semicolon.todo_list.dtos.responses.CheckOutTaskResponse;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.dtos.responses.UpdateTaskResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final TaskRepository taskRepository;
    private AuthenticationService authenticationService;

    public UserServiceImpl(UserRepository userRepository, AuthenticationServiceImpl authenticationServiceImpl, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.taskRepository = taskRepository;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        return authenticationServiceImpl.signUp(signUpRequest);
    }

    @Override
    public AddTaskResponse addTask(AddTaskRequest addTaskRequest) {
        checkIfTaskExisted(addTaskRequest.getTitle());
        User user = getCurrentUser();
        Task task = new Task();
        task.setTitle(addTaskRequest.getTitle());
        task.setDescription(addTaskRequest.getDescription());
        task.setPriority(addTaskRequest.getPriority());
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeadline(addTaskRequest.getDeadline());
        task.setUserId(user.getId());

        taskRepository.save(task);

        AddTaskResponse addTaskResponse = new AddTaskResponse();
        addTaskResponse.setTaskId(task.getId());
        addTaskResponse.setTaskDescription(task.getDescription());
        addTaskResponse.setTaskName(task.getTitle());
        addTaskResponse.setUserId(task.getUserId());
        addTaskResponse.setMessage("Task successfully added");
        return addTaskResponse;
    }

    private void checkIfTaskExisted(String title) {
        if (taskRepository.findTaskBy(title) != null) {
            throw new IllegalArgumentException("Task already exists");
        }
    }

    @Override
    public boolean login(LoginRequest loginRequest) {
        return authenticationServiceImpl.login(loginRequest);
    }

    @Override
    public CheckOutTaskResponse checkOutTask(CheckOutTaskRequest checkOutTaskRequest) {
        Task task = new Task();
        User user = getCurrentUser();
        task.setTitle(checkOutTaskRequest.getTitle());
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        task.setUserId(user.getId());

        taskRepository.save(task);

        CheckOutTaskResponse checkOutTaskResponse = new CheckOutTaskResponse();
        checkOutTaskResponse.setTaskId(checkOutTaskResponse.getTaskId());
        checkOutTaskResponse.setCompleted(checkOutTaskRequest.getCompleted());
        checkOutTaskResponse.setTaskName(checkOutTaskRequest.getTitle());
        checkOutTaskResponse.setMessage("Task completed");
        return checkOutTaskResponse;
    }

    @Override
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest) {
        Task task = new Task();
        task.setUserId(updateTaskRequest.getUserId());
        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setPriority(updateTaskRequest.getPriority());
        task.setCompleted(updateTaskRequest.getCompleted());
        task.setDeadline(updateTaskRequest.getDeadline());

        taskRepository.save(task);

        UpdateTaskResponse updateTaskResponse = new UpdateTaskResponse();
        updateTaskResponse.setTaskId(task.getId());
        updateTaskResponse.setTaskDescription(task.getDescription());
        updateTaskResponse.setTaskName(task.getTitle());
        updateTaskResponse.setMessage("Task update Successful");
        return updateTaskResponse;
    }


    private User getCurrentUser() {
        User user = new User();
        if(userRepository.findByUsername(user.getUsername()) != null || user.isLoggedin()) {
            return userRepository.findByUsername(user.getUsername());
        }
        return user;
    }





}
