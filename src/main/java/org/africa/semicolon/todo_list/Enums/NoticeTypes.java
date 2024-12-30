package org.africa.semicolon.todo_list.Enums;

import lombok.Getter;

@Getter
public enum NoticeTypes {
    TWO_HOURS_TO_TIME("Approximately 2 hours left to complete task!"),
    ONE_HOUR_TO_TIME("Approximately 1 hour to complete task!"),
    THIRTY_MINUTES_TO_TIME("Approximately 30 minutes to complete task!"),
    TWENTY_MINUTES_TO_TIME("Approximately 20 minutes to complete task!"),
    TEN_MINUTES_TO_TIME("Approximately 10 minutes to complete task!"),
    FIVE_MINUTES_TO_TIME("Approximately 5 minutes to complete task!"),
    IT_IS_TIME("Time Up; done with task?");

    final String message;
    NoticeTypes(String message) {
        this.message = message;
    }

    public String showMessage (boolean isSelected){
        return isSelected ? message : "";
    }
}
