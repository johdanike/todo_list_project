package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.Enums.Status;
import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final TaskRepository taskRepository;
    private final NotificationServiceImpl notificationServiceImpl;

    public UserServiceImpl(UserRepository userRepository, AuthenticationServiceImpl authenticationServiceImpl,
                           TaskRepository taskRepository, NotificationServiceImpl notificationServiceImpl) {
        this.userRepository = userRepository;
        this.authenticationServiceImpl = authenticationServiceImpl;
        this.taskRepository = taskRepository;
        this.notificationServiceImpl = notificationServiceImpl;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        return authenticationServiceImpl.signUp(signUpRequest);
    }

    @Override
    public AddTaskResponse addTask(AddTaskRequest addTaskRequest) {
        checkIfTaskExisted(addTaskRequest.getTitle());
        Task task = createNewTask(addTaskRequest);

        AddTaskResponse addTaskResponse = new AddTaskResponse();
        addTaskResponse.setTaskId(task.getId());
        addTaskResponse.setTaskDescription(addTaskRequest.getDescription());
        addTaskResponse.setTaskName(addTaskRequest.getTitle());
        addTaskResponse.setUserId(addTaskRequest.getUserId());
        addTaskResponse.setMessage("Task successfully added");
        addTaskResponse.setNotification(addTaskRequest.getNotification());
        addTaskResponse.setTaskStatus(task.getStatus());
        setReminders(addTaskRequest);
        return addTaskResponse;
    }

    private Task createNewTask(AddTaskRequest addTaskRequest) {
        Task task = new Task();
        task.setTitle(addTaskRequest.getTitle());
        task.setDescription(addTaskRequest.getDescription());
        task.setPriority(addTaskRequest.getPriority());
        task.setCreatedAt(LocalDateTime.now());
        task.setCompleted(false);
        task.setDeadline(addTaskRequest.getDeadline());
        task.setUserId(addTaskRequest.getUserId());
        task.setNotification(addTaskRequest.getNotification());
        task.setStatus(Status.CREATED);

        return taskRepository.save(task);
    }

    private void checkIfTaskExisted(String title) {
        if (taskRepository.findTaskBy(title) != null) throw new IllegalArgumentException("Task already exists");
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        return authenticationServiceImpl.login(loginRequest);
    }

    @Override
    public CheckOutTaskResponse checkOutTask(CheckOutTaskRequest checkOutTaskRequest) {
        CheckOutTaskResponse checkOutTaskResponse = new CheckOutTaskResponse();
        checkOutTaskResponse.setTaskId(checkOutTaskRequest.getTaskId());
        checkOutTaskResponse.setCompleted(checkOutTaskRequest.getCompleted());
        checkOutTaskResponse.setTaskName(checkOutTaskRequest.getTitle());
        checkOutTaskResponse.setMessage("Task completed");

        return checkOutTaskResponse;
    }

    @Override
    public UpdateTaskResponse updateTask(UpdateTaskRequest updateTaskRequest) {
        Task task = getTask(updateTaskRequest);

        taskRepository.save(task);

        UpdateTaskResponse updateTaskResponse = new UpdateTaskResponse();
        updateTaskResponse.setTaskId(task.getId());
        updateTaskResponse.setTaskDescription(task.getDescription());
        updateTaskResponse.setTaskName(task.getTitle());
        updateTaskResponse.setTaskStatus(task.getStatus());
        updateTaskResponse.setMessage("Task update Successful");
        return updateTaskResponse;
    }

    private Task getTask(UpdateTaskRequest updateTaskRequest) {
        Task task = getCurrentTask(updateTaskRequest.getTitle());
        task.setUserId(task.getUserId());
        task.setTitle(updateTaskRequest.getTitle());
        task.setDescription(updateTaskRequest.getDescription());
        task.setPriority(updateTaskRequest.getPriority());
        task.setCompleted(updateTaskRequest.getCompleted());
        task.setDeadline(updateTaskRequest.getDeadline());
        task.setId(updateTaskRequest.getTaskId());
        task.setStatus(updateTaskRequest.getTaskStatus());
        return task;
    }

    @Override
    public AddTaskResponse setReminders(AddTaskRequest addTaskRequest) {
        return notificationServiceImpl.setReminder(addTaskRequest);
    }

    @Override
    public LogOutResponse logOut(LogoutRequest logoutRequest) {
        return authenticationServiceImpl.logout(logoutRequest);
    }

    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getCurrentUser(changePasswordRequest.getUsername());
        
        if (user.isLoggedin()) {
            if (user.getPassword().equals(changePasswordRequest.getOldPassword())
                    && user.getUsername().equals(changePasswordRequest.getUsername())) {
                isPasswordAMatch(changePasswordRequest);

                user.setUsername(changePasswordRequest.getUsername());
                user.setPassword(changePasswordRequest.getNewPassword());
                user.setLoggedin(true);
                userRepository.save(user);
            }
        }
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse();
        changePasswordResponse.setMessage("Password successfully updated!");
        changePasswordResponse.setSuccess(true);
        changePasswordResponse.setUsername(user.getUsername());
        return changePasswordResponse;
    }

    private static void isPasswordAMatch(ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword()))
            throw new IllegalArgumentException("Passwords do not match");
    }

    @Override
    public DeleteTaskResponse deleteTask(DeleteTaskRequest deleteTaskRequest) {
        if (taskRepository.findTaskBy(deleteTaskRequest.getTaskName()) == null) {
            throw new IllegalArgumentException("Task does not exist");
        }
        Task task = taskRepository.findTaskBy(deleteTaskRequest.getTaskName());
        DeleteTaskResponse deleteTaskResponse = new DeleteTaskResponse();
        deleteTaskResponse.setTaskId(task.getId());
        deleteTaskResponse.setMessage("Task deleted successfully");
        deleteTaskResponse.setTaskName(task.getTitle());
        taskRepository.delete(task);
        return new DeleteTaskResponse();
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> viewAllTasksInProgress() {
        List<Task> inProgressTasks = new ArrayList<>();
        try {
            for (Task task : taskRepository.findAll()){
                if (task.getStatus().equals(Status.IN_PROGRESS))
                    inProgressTasks.add(task);
            }
        } catch (Exception e) {
            System.err.println("Error occurred while retrieving tasks: " + e.getMessage());
        }
        return inProgressTasks;
    }


    @Override
    public List<Task> viewAllCompletedTasks() {
        List<Task> completedTask = new ArrayList<>();
        try {
            for (Task task : taskRepository.findAll()) {
                if (task.getStatus().equals(Status.COMPLETED))
                    completedTask.add(task);
            }
        } catch (Exception e) {
            System.err.println("Error occurred while retrieving tasks: " + e.getMessage());
        }
        return completedTask;
    }

    private User getCurrentUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new IllegalArgumentException("User not found");
        return user;
    }

    private Task getCurrentTask(String title) {
        Task task = taskRepository.findTaskBy(title);
        if (task == null) throw new IllegalArgumentException("Task not found");
        return task;
    }

}
