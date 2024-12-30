package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.Enums.NoticeTypes;
import org.africa.semicolon.todo_list.data.models.Notification;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.dtos.requests.AddTaskRequest;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class NotificationServiceImplTest {
    @Autowired
    private NotificationServiceImpl notificationService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private NotificationServiceImpl notificationServiceImpl;
    private AddTaskRequest addTaskRequest;
    private Notification notification;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();

        notification = new Notification();
        notification.setStop(false);
        notification.setSnooze(false);
        notification.setNoticeTypes(NoticeTypes.THIRTY_MINUTES_TO_TIME);

        addTaskRequest = new AddTaskRequest();
        addTaskRequest.setTitle("Add Task");
        addTaskRequest.setDeadline("12/12/2025");
        addTaskRequest.setDescription("Add Task Description");
        addTaskRequest.setPriority("High");


    }

    @Test
    public void remindersCanBeSetAndSent_test(){
        addTaskRequest.setNotification(notification);

        AddTaskResponse addTaskResponse = userServiceImpl.addTask(addTaskRequest);
        assertNotNull(addTaskResponse);

        notification.setTaskId(addTaskResponse.getTaskId());
        addTaskRequest.setNotification(notification);


        AddTaskResponse addTaskResponse1 = notificationServiceImpl.setReminder(addTaskRequest);
        assertNotNull(addTaskResponse1);

    }
}