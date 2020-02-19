package ru.dgi.controller.result;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import ru.dgi.AuthorizedUser;
import ru.dgi.model.*;
import ru.dgi.service.*;
import java.time.LocalDateTime;
import java.util.List;
import static ru.dgi.util.ValidationUtil.*;

public class AbstractResultController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResultService resultService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private VariantService variantService;

    @Autowired
    private AnswerService answerService;

    public Result get(int id, int questId) {
        LOG.info("get " + id);
        return resultService.get(id, questId);
    }

    public List<Result> getAll(int questId) {
        LOG.info("getAll");
        return resultService.getAll(questId);
    }

    public long getCount(int questId) {
        LOG.info("getCount");
        return resultService.getCount(questId);
    }

    public Result getResultByQuestIdAndUsername(int questId) {
        LOG.info("getOneResultForUser");
        String username = AuthorizedUser.getUserName();
        return resultService.getResultByQuestIdAndUsername(questId, username);
    }

    public Result create(Result result) {
        LOG.info("create " + result);
        checkNew(result);
        result.setUsername(AuthorizedUser.getUserName());
        result.setFullname(AuthorizedUser.getFullName());
        result.setAnswerModified(LocalDateTime.now());
        return resultService.save(result);
    }

    public Result update(Result result, int id) {
        LOG.info("update " + result);
        assureIdConsistent(result, id);
        result.setAnswerModified(LocalDateTime.now());
        return resultService.update(result, id);
    }

    public Result saveEntireResult(Result result) {
        LOG.info("updateEntireResult " + result);

        Result savedResult = null;
        if (result.isNew()) {
            savedResult = create(result);
        } else {
            Result returnedResult = resultService.get(result.getId(), result.getQuest().getId());
            returnedResult.setStatus(result.getStatus());
            savedResult = update(returnedResult, returnedResult.getId());
        }

        int id = savedResult.getId();
        int questId = result.getQuest().getId();
        Result requestResult = resultService.get(id, questId);

        for (Answer answer : result.getAnswers())
        {
            answer.setResult(requestResult);
            Question question = answer.getQuestion();
            answer.setQuestion(questionService.get(question.getId(), questId));
            if (question.getAnswerTypeId() == 0 && answer.getVariant() != null) {
                Variant variant = answer.getVariant();
                answer.setVariant(variantService.get(variant.getId()));
            }
            answerService.save(answer);
        }

        return savedResult;

    }

    public void delete(int id, int questId) {
        LOG.info("delete " + id);
        Assert.notNull(id, "result must not be null");
        resultService.delete(id, questId);
    }
}
