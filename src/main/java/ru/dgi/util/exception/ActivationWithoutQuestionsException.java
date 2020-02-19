package ru.dgi.util.exception;

import org.springframework.http.HttpStatus;

public class ActivationWithoutQuestionsException extends ApplicationException {
    public static final String QUEST_HAS_NO_QUESTIONS_EXCEPTION = "exception.quest.activationWithoutQuestions";

    public ActivationWithoutQuestionsException() {
        super(ErrorType.VALIDATION_ERROR, QUEST_HAS_NO_QUESTIONS_EXCEPTION, HttpStatus.NOT_ACCEPTABLE);
    }
}
