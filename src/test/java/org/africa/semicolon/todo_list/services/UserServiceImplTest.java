package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.Enums.NoticeTypes;
import org.africa.semicolon.todo_list.data.models.Notification;
import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    private SignUpRequest signUpRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    private LoginRequest loginRequest;
    private AddTaskRequest addTaskRequest;
    private CheckOutTaskRequest checkOutTaskRequest;
    private UpdateTaskRequest updateTaskRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        taskRepository.deleteAll();

        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("username");
        signUpRequest.setPassword("password");
        signUpRequest.setEmail("email");
        signUpRequest.setName("name");


        loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");

        Notification notification = new Notification();
        notification.setStop(false);
        notification.setSnooze(false);
        notification.setNoticeTypes(NoticeTypes.THIRTY_MINUTES_TO_TIME);

        addTaskRequest = new AddTaskRequest();
        addTaskRequest.setTitle("Finish my To-Do app build");
        addTaskRequest.setDescription("description");
        addTaskRequest.setPriority("high");
        addTaskRequest.setNotification(notification);
        addTaskRequest.setDeadline("31/12/2024");

        checkOutTaskRequest = new CheckOutTaskRequest();
        checkOutTaskRequest.setCompleted(true);
        checkOutTaskRequest.setTitle("Finish my To-Do app build");

        updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("Finish my To-Do app build");
        updateTaskRequest.setDescription("description");
        updateTaskRequest.setPriority("Average");
        updateTaskRequest.setDeadline("07/01/2024");
        updateTaskRequest.setCompleted(false);

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setUsername("username");
        changePasswordRequest.setOldPassword("password");
        changePasswordRequest.setNewPassword("john-daniel");
        changePasswordRequest.setConfirmPassword("john-daniel");

    }

    @Test
    public void userCanRegister_countInUserRepositoryIsOne_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertEquals(1, userRepository.count());
        assertNotNull(signUpResponse);
    }
    @Test
    public void userRegistersUsingSameDetails_throwsException_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());
        assertEquals(signUpRequest.getUsername(), signUpResponse.getUsername());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->userService.signUp(signUpRequest));
        assertEquals("Account already exists", exception.getMessage());
        assertEquals(1, userRepository.count());
    }
    @Test
    public void userCanAddTasks_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());
        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());

    }
    @Test
    public void userCanCheckOutCompletedTasks_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());

        checkOutTaskRequest.setTaskId(addTaskResponse.getTaskId());
        CheckOutTaskResponse checkOutTaskResponse = userService.checkOutTask(checkOutTaskRequest);
        assertNotNull(checkOutTaskResponse);
        assertEquals("Task completed",checkOutTaskResponse.getMessage());
        assertEquals(1, taskRepository.count());
    }
    @Test
    public void userCannotAddSameTaskTwice_throwsException_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());
        assertEquals(signUpRequest.getUsername(), signUpResponse.getUsername());

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->userService.addTask(addTaskRequest));
        assertEquals("Task already exists", exception.getMessage());
        assertEquals(1, taskRepository.count());
    }
    @Test
    public void userCanUpdateTask_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());
        assertEquals(signUpRequest.getUsername(), signUpResponse.getUsername());

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());

        updateTaskRequest.setTaskId(addTaskResponse.getTaskId());
        UpdateTaskResponse updateTaskResponse = userService.updateTask(updateTaskRequest);
        assertTrue(updateTaskResponse != null);
        assertEquals("Task update Successful", updateTaskResponse.getMessage());
        assertEquals(1, taskRepository.count());
    }
    @Test
    public void tasksAdded_notificationCanBeSent_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);


        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());
        assertEquals(1, taskRepository.count());
        boolean isSelected = true;
        assertEquals("Approximately 2 hours left to complete task!", addTaskResponse.getNotification().getNoticeTypes().showMessage(isSelected));
    }
    @Test
    public void loggedInUserCanLogOut_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertEquals("Finish my To-Do app build", addTaskResponse.getTaskName());

        assertEquals(1, taskRepository.count());
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername(loginRequest.getUsername());
        logoutRequest.setPassword(loginRequest.getPassword());
        LogOutResponse logoutResponse = userService.logOut(logoutRequest);
        String message = logoutResponse.getMessage();
        assertEquals("Logout successful", userService.logOut(logoutRequest).getMessage());
    }
    @Test
    public void userCanChangePassword_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());
        ChangePasswordResponse changePasswordResponse = userService.changePassword(changePasswordRequest);
        assertNotNull(changePasswordResponse);
        assertEquals("Password successfully updated!", changePasswordResponse.getMessage());
    }
    @Test
    public void userCanLoginWithNewPassword_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        ChangePasswordResponse changePasswordResponse = userService.changePassword(changePasswordRequest);
        assertNotNull(changePasswordResponse);
        assertEquals("username", changePasswordResponse.getUsername());

        LoginRequest loginRequest1 = new LoginRequest();
        loginRequest1.setUsername("username");
        loginRequest1.setPassword("john-daniel");

        LoginResponse loginResponse2 = userService.login(loginRequest1);
        assertEquals("Logged In Successfully", loginResponse2.getMessage());
    }
    @Test
    public void userCannotLoginWithOldPassword_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        ChangePasswordResponse changePasswordResponse = userService.changePassword(changePasswordRequest);
        assertNotNull(changePasswordResponse);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->userService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());
    }
    @Test
    public void userCanDeleteTask_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

        DeleteTaskRequest deleteTaskRequest = new DeleteTaskRequest();
        deleteTaskRequest.setTaskId(addTaskResponse.getTaskId());
        deleteTaskRequest.setTaskName(addTaskResponse.getTaskName());
        deleteTaskRequest.setMessage("Task deleted successfully");
        DeleteTaskResponse deleteTaskResponse = userService.deleteTask(deleteTaskRequest);
        assertNotNull(deleteTaskResponse);
        assertEquals(0, taskRepository.count());

        List<Task> tasks = taskRepository.findAll();
        assertNotNull(tasks);
        assertEquals(0, tasks.size());
    }
    @Test
    public void userCanViewAllTasks_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());


        LoginResponse loginResponse = userService.login(loginRequest);
        assertEquals("Logged In Successfully", loginResponse.getMessage());

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

        List<Task> tasks = userService.findAll();
        assertNotNull(tasks);
//        System.out.println(tasks);
        assertEquals(1, tasks.size());
    }
}
