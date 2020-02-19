package ru.dgi.controller.question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dgi.model.Question;
import ru.dgi.service.QuestionService;
import ru.dgi.to.QuestionStatistics;

import java.util.List;

import static ru.dgi.util.ValidationUtil.*;

public class AbstractQuestionController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private QuestionService questionService;

    public Question get(int id, int questId) {
        LOG.info("get " + id);
        return questionService.get(id, questId);
    }

    public List<Question> getAll(int questId) {
        LOG.info("getAll");
        return questionService.getAll(questId);
    }

    public Question getActive(int id, int questId) {
        LOG.info("getActive " + id);
        return questionService.getActive(id, questId);
    }

    public List<Question> getAllActive(int questId) {
        LOG.info("getAllActive");
        return questionService.getAllActive(questId);
    }

    public List<QuestionStatistics> getQuestionStatistics(int questId) {
        LOG.info("getQuestionStatistics");
        return questionService.getQuestionStatistics(questId);
    }

    public Question create(Question question) {
        LOG.info("create " + question);
        checkNew(question);
        return questionService.save(question);
    }

    public Question update(Question question, int id) {
        LOG.info("update " + question);
        assureIdConsistent(question, id);
        return questionService.update(question, id);
    }

    public void delete(int id, int questId) {
        LOG.info("delete " + id);
        questionService.delete(id, questId);
    }
}
