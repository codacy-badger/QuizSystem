package ru.dgi.util.exception;

import org.springframework.http.HttpStatus;

public class QuestAlreadyCompletedException extends ApplicationException {
    public static final String QUEST_ALREADY_COMPLETED_EXCEPTION = "exception.quest.alreadyCompleted";

    public QuestAlreadyCompletedException() {
        super(ErrorType.WRONG_REQUEST, QUEST_ALREADY_COMPLETED_EXCEPTION, HttpStatus.BAD_REQUEST);
    }
}
