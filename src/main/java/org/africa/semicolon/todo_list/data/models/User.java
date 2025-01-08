package org.africa.semicolon.todo_list.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document

public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private boolean isLoggedin;
    private List<Task> tasks;
}
