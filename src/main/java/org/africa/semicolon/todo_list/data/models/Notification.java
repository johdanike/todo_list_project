package org.africa.semicolon.todo_list.data.models;

import lombok.Data;
import org.africa.semicolon.todo_list.Enums.NoticeTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Notification {
    @Id
    private String id;
    private String taskId;
    private Boolean snooze;
    private Boolean stop;
    private NoticeTypes noticeTypes;
}
