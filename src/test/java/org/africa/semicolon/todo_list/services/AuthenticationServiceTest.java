package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.LoginRequest;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.exceptions.InvalidUsernameOrPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserRepository userRepository;
    private SignUpRequest signUpRequest;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("johdanike@gmail.com");
        signUpRequest.setPassword("password");
        signUpRequest.setUsername("johdanike");
        signUpRequest.setName("John-Daniel Ikechukwu");

    }

    @Test
    public void userCanRegister_test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);
        assertEquals(1, userRepository.count());
    }

    @Test
    public void alreadyRegisteredUserCannotRegister_throwsException_Test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp(signUpRequest));
        assertEquals("Account already exists", exception.getMessage());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void userCannotRegisterWithTheSameEmail_throwsException_Test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authenticationService.signUp(signUpRequest));
        assertEquals("Account already exists", exception.getMessage());
        assertEquals(1, userRepository.count());
    }

    @Test
    public void registeredUserCanLogin_test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("johdanike");
        loginRequest.setPassword("password");
        Boolean loginResponse = authenticationService.login(loginRequest);
        assertTrue(loginResponse);
        assertTrue(userRepository.count() == 1);
    }

    @Test
    public void userCannotLoginWithWrongCredentials_throwsException_Test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("johdanike");
        loginRequest.setPassword("passworgfs");
        InvalidUsernameOrPasswordException exception = assertThrows(InvalidUsernameOrPasswordException.class, ()->authenticationService.login(loginRequest));
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    public void loggedInUserCanLogout_test(){
        SignUpResponse signUpResponse = authenticationService.signUp(signUpRequest);
        assertNotNull(signUpResponse);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("johdanike");
        loginRequest.setPassword("password");

        Boolean loginResponse = authenticationService.login(loginRequest);
        assertTrue(loginResponse);

        Boolean logOutResponse = authenticationService.logout();
        assertTrue(logOutResponse);
    }

}