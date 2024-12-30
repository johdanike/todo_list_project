package org.africa.semicolon.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.dtos.requests.*;
import org.africa.semicolon.todo_list.dtos.responses.*;
import org.africa.semicolon.todo_list.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        try {
            SignUpResponse signUpResponse = userService.signUp(signUpRequest);
            return new  ResponseEntity<>(new AuthenticationApiResponse(true, signUpResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/addTask/")
    public ResponseEntity<?> addTask(@RequestBody AddTaskRequest addTaskRequest) {
        try {
            AddTaskResponse addTaskResponse = userService.addTask(addTaskRequest);
            return new ResponseEntity<>(new AuthenticationApiResponse(true, addTaskResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Boolean loginResponse = userService.login(loginRequest);
            return new ResponseEntity<>(new AuthenticationApiResponse(true, loginResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateTask/")
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest updateTaskRequest) {
        try {
            UpdateTaskResponse updateTaskResponse = userService.updateTask(updateTaskRequest);
            return new ResponseEntity<>(new AuthenticationApiResponse(true, updateTaskResponse), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteTask/")
    public ResponseEntity<?> deleteTask(@RequestBody DeleteTaskRequest deleteTaskRequest) {
        try{
            DeleteTaskResponse deleteTaskResponse = userService.deleteTask(deleteTaskRequest);
            return new ResponseEntity<>(new AuthenticationApiResponse(true, deleteTaskResponse), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new AuthenticationApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/tasks/", method = RequestMethod.GET)
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> findAll = userService.findAll();
        return new ResponseEntity<>(findAll, new HttpHeaders(), HttpStatus.OK);
    }



}
