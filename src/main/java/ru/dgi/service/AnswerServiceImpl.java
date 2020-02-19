package ru.dgi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.dgi.dao.AnswerRepository;
import ru.dgi.to.AnswerTo;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Answer;
import java.util.List;
import static ru.dgi.util.ValidationUtil.checkNotFoundWithId;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public List<Answer> getAll(int resultId) {
        return answerRepository.getAll(resultId);
    }

    @Override
    public AnswerTo getOneMoreThanId(int id, int questionId) {
        return answerRepository.getOneMoreThanId(id, questionId);
    }

    @Override
    public Answer save(Answer answer) {
        Assert.notNull(answer, "answer must not be null");
        return answerRepository.save(answer);
    }

    @Override
    public Answer update(Answer answer, int id) throws NotFoundException {
        Assert.notNull(answer, "answer must not be null");
        return checkNotFoundWithId(answerRepository.save(answer),id);
    }

    @Override
    public void delete(int id, int resultId) throws NotFoundException {
        Assert.notNull(id, "answer must not be null");
        checkNotFoundWithId(answerRepository.delete(id, resultId) != 0, id);
    }
}
