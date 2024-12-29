package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.SignUpRequest;
import org.africa.semicolon.todo_list.dtos.responses.SignUpResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public SignUpResponse signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setPassword(signUpRequest.getPassword());
        user.setEmail(signUpRequest.getEmail());
        user.setIsLoggedin(false);
        user.setUsername(signUpRequest.getUsername());
        userRepository.save(user);

        SignUpResponse signUpResponse = new SignUpResponse();
        signUpResponse.setMessage("Sign up successful");
        signUpResponse.setUsername(user.getUsername());
        signUpResponse.setId(user.getId());
        return signUpResponse;
    }
}
