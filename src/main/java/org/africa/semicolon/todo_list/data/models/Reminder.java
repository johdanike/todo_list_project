package org.africa.semicolon.todo_list.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document
public class Reminder {
    @Id
    private String id;
    private Boolean snooze;
    private Boolean stop;
    private LocalDateTime date;

}
