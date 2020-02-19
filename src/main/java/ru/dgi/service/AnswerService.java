package ru.dgi.service;

import ru.dgi.to.AnswerTo;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Answer;

import java.util.List;

public interface AnswerService {

    List<Answer> getAll(int resultId);

    AnswerTo getOneMoreThanId(int id, int questionId);

    Answer save(Answer answer);

    Answer update(Answer answer, int id) throws NotFoundException;

    void delete(int id, int resultId) throws NotFoundException;
}
