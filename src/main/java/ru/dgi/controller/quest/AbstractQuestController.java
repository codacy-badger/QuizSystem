package ru.dgi.controller.quest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dgi.AuthorizedUser;
import ru.dgi.model.*;
import ru.dgi.service.QuestService;
import ru.dgi.service.QuestionService;
import ru.dgi.service.ResultService;
import ru.dgi.service.VariantService;
import ru.dgi.to.QuestWithResult;
import ru.dgi.util.exception.ModificationQuestAnswerExistsException;
import ru.dgi.util.exception.ActivationWithoutQuestionsException;
import ru.dgi.util.exception.QuestAlreadyCompletedException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static ru.dgi.util.ValidationUtil.*;

public class AbstractQuestController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    protected QuestService questService;

    @Autowired
    protected QuestionService questionService;

    @Autowired
    protected VariantService variantService;

    @Autowired
    protected ResultService resultService;

    public Quest get(int id) {
        LOG.info("get " + id);
        return questService.get(id);
    }

    public Quest getActive(int id) {
        LOG.info("getActive " + id);
        String username = AuthorizedUser.getUserName();
        Result result = resultService.getResultByQuestIdAndUsername(id, username);
        if (result != null && result.getStatus() == Status.COMPLETED)
            throw new QuestAlreadyCompletedException();
        return questService.getActive(id);
    }

    public List<Quest> getAll() {
        LOG.info("getAll");
        return questService.getAll();
    }

    public List<QuestWithResult> getAllActiveOrStarted() {
        LOG.info("getAllActiveOrStarted");
        String username = AuthorizedUser.getUserName();
        return questService.getAllActiveOrStarted(username);
    }

    public Quest create(Quest quest) {
        LOG.info("create " + quest);
        checkNew(quest);
        checkActivationWithoutQuestions(quest);
        return questService.save(quest);
    }

    public Quest update(Quest quest, int id) {
        LOG.info("update " + quest);
        assureIdConsistent(quest, id);
        checkModificationQuestAnswerExists(quest, id);
        checkActivationWithoutQuestions(quest);
        return questService.update(quest, id);
    }

    public void deleteEntireQuest(int id) {
        LOG.info("delete entire quest " + id);
        questService.deleteEntireQuest(id);
    }

    public void delete(int id) {
        LOG.info("delete " + id);
        questService.delete(id);
    }

    public void enable(int id, boolean isActive) {
        LOG.info((isActive ? "activated " : "disactivated ") + id);
        checkActivationWithoutQuestions(id, isActive);
        questService.enable(id, isActive);
    }

    private void checkActivationWithoutQuestions(Quest quest) {
        if ((quest.getQuestions() == null || quest.getQuestions().size() == 0) && quest.isActive()) {
            throw new ActivationWithoutQuestionsException();
        }
    }

    private void checkActivationWithoutQuestions(int id, boolean isActive) {
        Quest quest = questService.get(id);
        if ((quest.getQuestions() == null || quest.getQuestions().size() == 0) && isActive) {
            throw new ActivationWithoutQuestionsException();
        }
    }

    private void checkModificationQuestAnswerExists(Quest quest, int id) {
        if (resultService.getCount(id) != 0) {
            List<Question> existedQuestions = get(id).getQuestions();

            quest.getQuestions().stream()
                    .forEach(q -> {
                        if (q.getId() == null) {
                            throw new ModificationQuestAnswerExistsException();
                        }
                    });
            Map<Integer, Question> updatedQuestions = quest.getQuestions().stream()
                    .collect(Collectors.toMap(Question::getId, q -> q));
            existedQuestions.stream()
                    .forEach(q -> {
                        if (updatedQuestions.get(q.getId()) == null || !q.getName().equals(updatedQuestions.get(q.getId()).getName())) {
                            throw new ModificationQuestAnswerExistsException();
                        }
                    });

            quest.getQuestions().stream()
                    .filter(q -> q.getVariants() != null)
                    .flatMap(q -> q.getVariants().stream())
                    .forEach(v -> {
                        if (v.getId() == null) {
                            throw new ModificationQuestAnswerExistsException();
                        }
                    });
            Map<Integer, Variant> updatedVariants = quest.getQuestions().stream()
                    .filter(q -> q.getVariants() != null)
                    .flatMap(q -> q.getVariants().stream())
                    .collect(Collectors.toMap(Variant::getId, v -> v));
            existedQuestions.stream()
                    .flatMap(q -> q.getVariants().stream())
                    .forEach(v -> {
                        if (updatedVariants.get(v.getId()) == null || !v.getName().equals(updatedVariants.get(v.getId()).getName())) {
                            throw new ModificationQuestAnswerExistsException();
                        }
                    });

        }
    }
}
