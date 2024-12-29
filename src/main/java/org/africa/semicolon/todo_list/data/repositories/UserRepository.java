package org.africa.semicolon.todo_list.data.repositories;

import org.africa.semicolon.todo_list.data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
