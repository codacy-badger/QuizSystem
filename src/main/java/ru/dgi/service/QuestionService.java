package ru.dgi.service;

import ru.dgi.to.QuestionStatistics;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Question;
import java.util.List;

public interface QuestionService {

    Question get(int id, int questId) throws NotFoundException;

    List<Question> getAll(int questId);

    Question getActive(int id, int questId) throws NotFoundException;

    List<Question> getAllActive(int questId);

    List<QuestionStatistics> getQuestionStatistics(int questId);

    Question save(Question question);

    Question update(Question question, int id) throws NotFoundException;

    void delete(int id, int questId) throws NotFoundException;
}
