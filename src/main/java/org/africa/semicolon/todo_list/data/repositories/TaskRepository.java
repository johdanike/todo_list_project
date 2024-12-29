package org.africa.semicolon.todo_list.data.repositories;

import org.africa.semicolon.todo_list.data.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task, String> {
}
