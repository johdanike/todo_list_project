package org.africa.semicolon.todo_list.data.repositories;

import org.africa.semicolon.todo_list.data.models.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {
}
