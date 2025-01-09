package org.africa.semicolon.todo_list.data.repositories;

import org.africa.semicolon.todo_list.Enums.Status;
import org.africa.semicolon.todo_list.data.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    Task findTaskBy(String title);
    Task findTaskByTitle(String title);
    List<Task> findAllBy(Status status);
}
