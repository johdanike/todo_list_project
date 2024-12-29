package org.africa.semicolon.todo_list.data.repositories;

import org.africa.semicolon.todo_list.data.models.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReminderRepository extends MongoRepository<Reminder, String> {
}
