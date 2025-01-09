package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.responses.LogOutResponse;
import org.africa.semicolon.todo_list.dtos.requests.LoginRequest;
import org.africa.semicolon.todo_list.dtos.requests.LogoutRequest;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.LoginResponse;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        checkIfUserAlreadyExists(signUpRequest.getUsername());
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setLoggedin(false);
        user.setUsername(signUpRequest.getUsername());
        userRepository.save(user);

        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setMessage("Sign up successful");
        signUpResponse.setUsername(user.getUsername());
        signUpResponse.setId(user.getId());
        return signUpResponse;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (isloginRequestEmptyOrNull(loginRequest) ||
                containsWhiteSpace(loginRequest.getPassword()) ||
                containsWhiteSpace(loginRequest.getUsername())) {
            throw new IllegalArgumentException("Username or password fields cannot be empty and cannot contain any spaces");
        }
        if(user == null) {
            throw new UserNotFoundException("User not found");
        }

        LoginResponse response = userDetailsIsCorrect_login(loginRequest, user);
        if (response != null) {
            userRepository.save(user);
            return response;
        }
        throw new IllegalArgumentException("Invalid username or password");
    }



    private static LoginResponse userDetailsIsCorrect_login(LoginRequest loginRequest, User user) {
        if (user.getUsername().equals(loginRequest.getUsername()) && user.getPassword().equals(loginRequest.getPassword())) {
            user.setLoggedin(true);
            LoginResponse response = new LoginResponse();
            response.setUsername(user.getUsername());
            response.setMessage("Logged In Successfully");

            return response;
        }
        return null;
    }

    private static boolean isloginRequestEmptyOrNull(LoginRequest loginRequest) {
        return loginRequest.getPassword() == null ||
                loginRequest.getUsername().isEmpty() ||
                loginRequest.getPassword().isEmpty();
    }

    private static boolean containsWhiteSpace(String string){
        Pattern pattern = Pattern.compile("(.*?)\\s(.*?)");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }

    @Override
    public LogOutResponse logout(LogoutRequest logoutRequest){
        User user = getCurrentUser(logoutRequest.getUsername());
        user.setLoggedin(false);
        LogOutResponse logOutResponse = new LogOutResponse();
        logOutResponse.setMessage("Logout successful");
        userRepository.save(user);
        return logOutResponse;
    }
    private User getCurrentUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    private void checkIfUserAlreadyExists(String username) {
        if (userRepository.findByUsername(username) != null && userRepository.findByUsername(username).getEmail() != null) {
            throw new IllegalArgumentException("Account already exists");
        }
    }
}
