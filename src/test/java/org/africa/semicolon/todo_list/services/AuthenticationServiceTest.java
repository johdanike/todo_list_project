package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void userCanRegister_test(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("johdanike@gmail.com");
        signUpRequest.setPassword("password");
        signUpRequest.setUsername("johdanike");
        signUpRequest.setName("John-Daniel Ikechukwu");

        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());
    }



}