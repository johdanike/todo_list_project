package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.LoginRequest;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.africa.semicolon.todo_list.exceptions.InvalidUsernameOrPasswordException;
import org.springframework.stereotype.Service;

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
    public Boolean login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null || !user.getPassword().equals(loginRequest.getPassword()) || !user.getUsername().equals(loginRequest.getUsername())) {
            throw new InvalidUsernameOrPasswordException("Invalid username or password");
        }
        user.setLoggedin(true);
        userRepository.save(user);
        return user.isLoggedin();
    }

    @Override
    public Boolean logout() {
        User user = new User();
        user.setLoggedin(false);
        userRepository.save(user);
        return true;
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
