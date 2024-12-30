package org.africa.semicolon.todo_list.controllers;

import lombok.RequiredArgsConstructor;
import org.africa.semicolon.todo_list.dtos.requests.LoginRequest;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.AuthenticationApiResponse;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup/")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        try{
            SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
            return new ResponseEntity<>(new AuthenticationApiResponse(true, signUpResponse), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(new SignUpResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login/")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Boolean loginResponse = authenticationService.login(loginRequest);
                return new ResponseEntity<>(new AuthenticationApiResponse(true,loginResponse), HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout/")
    public ResponseEntity<?> logout() {
        try {
            Boolean logoutResponse = authenticationService.logout();
            return new ResponseEntity<>(new AuthenticationApiResponse(true,logoutResponse), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
