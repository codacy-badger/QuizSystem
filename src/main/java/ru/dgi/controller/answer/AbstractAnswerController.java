package ru.dgi.controller.answer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dgi.model.Answer;
import ru.dgi.service.AnswerService;
import ru.dgi.to.AnswerTo;
import java.util.List;
import static ru.dgi.util.ValidationUtil.*;

public class AbstractAnswerController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private AnswerService answerService;

    public List<Answer> getAll(int resultId) {
        LOG.info("getAll");
        return answerService.getAll(resultId);
    }

    public AnswerTo getOneMoreThanId(int id, int questionId) {
        LOG.info("getOneMoreThanId");
        return answerService.getOneMoreThanId(id, questionId);
    }

    public Answer create(Answer answer) {
        LOG.info("create " + answer);
        checkNew(answer);
        return answerService.save(answer);
    }

    public Answer update(Answer answer, int id) {
        LOG.info("update " + answer);
        assureIdConsistent(answer, id);
        return answerService.update(answer, id);
    }

    public void delete(int id, int resultId) {
        LOG.info("delete " + id);
        answerService.delete(id, resultId);
    }
}
