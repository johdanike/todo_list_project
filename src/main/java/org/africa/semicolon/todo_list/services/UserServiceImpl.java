package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.Notification;
import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final TaskRepository taskRepository;

    private NotificationServiceImpl notificationServiceImpl;
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
        Task task = createNewTask(addTaskRequest);


        addTaskRequest.setPriority(task.getPriority());
        addTaskRequest.setDescription(task.getDescription());
        addTaskRequest.setDeadline(task.getDeadline());
        addTaskRequest.setTitle(task.getTitle());
        addTaskRequest.setNotification(task.getNotification());

        Notification notification = getNotification(addTaskRequest);
        AddTaskResponse addTaskResponse = new AddTaskResponse();
        addTaskResponse.setTaskId(task.getId());
        addTaskResponse.setTaskDescription(task.getDescription());
        addTaskResponse.setTaskName(task.getTitle());
        addTaskResponse.setUserId(task.getUserId());
        addTaskResponse.setMessage("Task successfully added");
        addTaskResponse.setNotification(notification);
        return addTaskResponse;
    }

    private static Notification getNotification(AddTaskRequest addTaskRequest) {
        Notification notification = new Notification();
        notification.setSnooze(addTaskRequest.getNotification().getSnooze());
        notification.setStop(addTaskRequest.getNotification().getStop());
        notification.setTaskId(addTaskRequest.getNotification().getTaskId());
        notification.setNoticeTypes(addTaskRequest.getNotification().getNoticeTypes());

        return notification;
    }

    private Task createNewTask(AddTaskRequest addTaskRequest) {
        User user = getCurrentUser();
        Task task = new Task();
        task.setTitle(addTaskRequest.getTitle());
        task.setDescription(addTaskRequest.getDescription());
        task.setPriority(addTaskRequest.getPriority());
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeadline(addTaskRequest.getDeadline());
        task.setUserId(user.getId());
        task.setNotification(getNotification(addTaskRequest));

        taskRepository.save(task);
        return task;
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
        getDetailsForTaskUpdate(checkOutTaskRequest);

        CheckOutTaskResponse checkOutTaskResponse = new CheckOutTaskResponse();
        checkOutTaskResponse.setTaskId(checkOutTaskResponse.getTaskId());
        checkOutTaskResponse.setCompleted(checkOutTaskRequest.getCompleted());
        checkOutTaskResponse.setTaskName(checkOutTaskRequest.getTitle());
        checkOutTaskResponse.setMessage("Task completed");
        return checkOutTaskResponse;
    }

    private void getDetailsForTaskUpdate(CheckOutTaskRequest checkOutTaskRequest) {
        UpdateTaskRequest updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle(checkOutTaskRequest.getTitle());
        updateTaskRequest.setTaskId(checkOutTaskRequest.getTaskId());
        updateTaskRequest.setPriority(updateTaskRequest.getPriority());
        updateTaskRequest.setDescription(updateTaskRequest.getDescription());
        updateTaskRequest.setDeadline(updateTaskRequest.getDeadline());
        updateTaskRequest.setCompleted(updateTaskRequest.getCompleted());

        updateTask(updateTaskRequest);
    }

    @Override
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest) {
        Task task = getTask(updateTaskRequest);

        taskRepository.save(task);

        UpdateTaskResponse updateTaskResponse = new UpdateTaskResponse();
        updateTaskResponse.setTaskId(task.getId());
        updateTaskResponse.setTaskDescription(task.getDescription());
        updateTaskResponse.setTaskName(task.getTitle());
        updateTaskResponse.setMessage("Task update Successful");
        return updateTaskResponse;
    }

    private Task getTask(UpdateTaskRequest updateTaskRequest) {
        User user = getCurrentUser();
        Task task = getCurrentTask();
        task.setUserId(user.getId());
        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setPriority(updateTaskRequest.getPriority());
        task.setCompleted(updateTaskRequest.getCompleted());
        task.setDeadline(updateTaskRequest.getDeadline());
        task.setId(task.getId());
        return task;
    }

    @Override
    public AddTaskResponse getReminders(AddTaskRequest addTaskRequest) {
        return notificationServiceImpl.setReminder(addTaskRequest);
    }

    private User getCurrentUser() {
        User user = new User();
        if(userRepository.findByUsername(user.getUsername()) != null || user.isLoggedin()) {
            return userRepository.findByUsername(user.getUsername());
        }
        return user;
    }

    private Task getCurrentTask() {
        Task task = new Task();
        if (taskRepository.findTaskBy(task.getTitle()) != null) {
            return taskRepository.findTaskBy(task.getTitle());
        }
        return task;
    }





}
