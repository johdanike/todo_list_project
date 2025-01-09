package org.africa.semicolon.todo_list.services;

import org.africa.semicolon.todo_list.Enums.NoticeTypes;
import org.africa.semicolon.todo_list.data.models.Notification;
import org.africa.semicolon.todo_list.data.models.Task;
import org.africa.semicolon.todo_list.data.models.User;
import org.africa.semicolon.todo_list.data.repositories.NotificationRepository;
import org.africa.semicolon.todo_list.data.repositories.TaskRepository;
import org.africa.semicolon.todo_list.data.repositories.UserRepository;
import org.africa.semicolon.todo_list.dtos.requests.AddTaskRequest;
import org.africa.semicolon.todo_list.dtos.responses.AddTaskResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddTaskResponse setReminder(AddTaskRequest addTaskRequest) {
        validateCurrentUserAndCurrentTask currentUserAndTask = getValidateCurrentUserAndCurrentTask(addTaskRequest);

        Notification notification = getNotification(addTaskRequest, currentUserAndTask.currentTask());

        AddTaskResponse addTaskResponse = new AddTaskResponse();
        addTaskResponse.setTaskId(currentUserAndTask.currentTask().getId());
        addTaskResponse.setTaskDescription(currentUserAndTask.currentTask().getDescription());
        addTaskResponse.setTaskName(addTaskRequest.getTitle());
        addTaskResponse.setUserId(currentUserAndTask.currentUser().getId());
        addTaskResponse.setNotification(notification);
        return addTaskResponse;
    }

    @Override
    public AddTaskResponse setNewReminder(AddTaskRequest addTaskRequest) {
        validateCurrentUserAndCurrentTask currentUserAndTask = getValidateCurrentUserAndCurrentTask(addTaskRequest);

//        if (addTaskRequest.getNotification()){
//            Notification notification = new Notification();
//            notification.setTaskId(currentUserAndTask.currentTask().getId());
//
//        }
        return setReminder(addTaskRequest);
    }

    private validateCurrentUserAndCurrentTask getValidateCurrentUserAndCurrentTask(AddTaskRequest addTaskRequest) {
        if (addTaskRequest.getNotification() == null)
            addTaskRequest.setNotification(new Notification());
        User currentUser = getCurrentUser();
        if (currentUser == null)
            throw new IllegalArgumentException("Current user is not logged in or does not exist.");
        Task currentTask = getCurrentTask();
        if (currentTask == null)
            throw new IllegalArgumentException("Task not found.");
        validateCurrentUserAndCurrentTask currentUserAndTask = new validateCurrentUserAndCurrentTask(currentUser, currentTask);
        return currentUserAndTask;
    }

    private record validateCurrentUserAndCurrentTask(User currentUser, Task currentTask) {
    }

    private Notification getNotification(AddTaskRequest addTaskRequest, Task currentTask) {
        Notification notification = addTaskRequest.getNotification();
        for (NoticeTypes noticeType : NoticeTypes.values()) {
            boolean isSelected = true;
            String message = noticeType.showMessage(isSelected);
            if (!message.isEmpty()) {
                System.out.println(message);
                notification.setNoticeTypes(noticeType);
                break;
            }
        }

        notification.setStop(false);
        notification.setSnooze(false);
        notification.setTaskId(currentTask.getId());
        notificationRepository.save(notification);
        return notification;
    }

    private User getCurrentUser() {
        User user = new User();
        if(userRepository.findByUsername(user.getUsername()) != null || user.isLoggedin()) {
            return userRepository.findByUsername(user.getUsername());
        }
        return user;
    }

    private Task getCurrentTask() {
        Task task = new Task();
        if (taskRepository.findTaskBy(task.getTitle()) != null) {
            return taskRepository.findTaskBy(task.getTitle());
        }
        return task;
    }



}
