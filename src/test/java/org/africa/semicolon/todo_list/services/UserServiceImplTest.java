package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.Notification;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;
import org.africa.semicolon.todo_list.dtos.responses.CheckOutTaskResponse;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.dtos.responses.UpdateTaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userService;
    private SignUpRequest signUpRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    private AddTaskRequest addTaskRequest;
    @Autowired
    private TaskRepository taskRepository;
    private LoginRequest loginRequest;
    private CheckOutTaskRequest checkOutTaskRequest;
    private UpdateTaskRequest updateTaskRequest;

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

        assertTrue(userService.login(loginRequest));

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);
    }

    @Test
    public void userCanCheckOutCompletedTasks_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());

        assertTrue(userService.login(loginRequest));

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

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

        assertTrue(userService.login(loginRequest));

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

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

        assertTrue(userService.login(loginRequest));

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

        updateTaskRequest.setTaskId(addTaskResponse.getTaskId());
        UpdateTaskResponse updateTaskResponse = userService.updateTask(updateTaskRequest);
        assertTrue(updateTaskResponse != null);
        assertEquals("Task update Successful", updateTaskResponse.getMessage());
        assertEquals(1, taskRepository.count());
    }

    @Test
    public void tasksAddedSensNotification_test(){
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        assertTrue(userService.login(loginRequest));

        AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);
        assertEquals(1, taskRepository.count());


    }
}
