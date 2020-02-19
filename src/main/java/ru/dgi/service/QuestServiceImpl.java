package ru.dgi.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.dgi.dao.QuestRepository;
import ru.dgi.dao.QuestionRepository;
import ru.dgi.dao.ResultRepository;
import ru.dgi.dao.VariantRepository;
import ru.dgi.model.*;
import ru.dgi.to.QuestWithResult;
import ru.dgi.util.exception.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static ru.dgi.util.ValidationUtil.checkNotFoundWithId;

@Service
public class QuestServiceImpl implements QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public Quest get(int id) throws NotFoundException {
        return checkNotFoundWithId(questRepository.findById(id).orElse(null), id);
    }

    @Override
    public Quest getActive(int id) {
        return checkNotFoundWithId(questRepository.getActive(id), id);
    }

    @Override
    public List<Quest> getAll() {
        return questRepository.findAll();
    }

    @Override
    public List<QuestWithResult> getAllActiveOrStarted(String username) {

        List<Quest> quests = questRepository.getAllActiveOrStarted(username);
        List<QuestWithResult> questsWithResult = new ArrayList<>();
        for (Quest quest : quests) {
            int questId = quest.getId();
            Result result = resultRepository.getResultByQuestIdAndUsername(questId, username);
            if (result != null)
                questsWithResult.add(new QuestWithResult(questId, result.getStatus(), quest.getName(), quest.isActive()));
            else
            {
                questsWithResult.add(new QuestWithResult(questId, Status.UNCOMPLETED, quest.getName(), quest.isActive()));
            }
        }

        return questsWithResult;
    }

    private Quest prepareForPersist(Quest quest) {
        if (quest.getQuestions() != null) {
            quest.getQuestions().stream()
                    .filter(q -> q.getVariants() != null)
                    .flatMap(q -> q.getVariants().stream())
                    .forEach(v -> v.setName(v.getName().trim()));

            quest.getQuestions().stream()
                .filter(q -> q.getVariants() != null)
                .forEach(q -> q.setVariants(
                        q.getVariants().stream()
                        .map(v -> variantRepository.getByName(v.getName()) != null ?
                                variantRepository.getByName(v.getName()) : v)
                        .collect(Collectors.toList())
                ));

            quest.getQuestions()
                    .forEach(q -> q.setQuest(quest));
        }
        return quest;
    }

    private List<Integer> getDeletedQuestions(Quest quest, int id) {
        // getting posted questions
        List<Integer> requestQuestions = CollectionUtils.emptyIfNull(quest.getQuestions()).stream()
                .filter(q -> q.getId() != null)
                .map(Question::getId)
                .collect(Collectors.toList());

        // getting deleted questions
        List<Integer> deletedQuestions = CollectionUtils.emptyIfNull(get(id).getQuestions()).stream()
                .filter(q -> !requestQuestions.contains(q.getId()))
                .map(Question::getId)
                .collect(Collectors.toList());
        return deletedQuestions;
    }

    public void deleteQuestions(List<Integer> questionsToDelete, int id) {
        questionsToDelete.stream()
                .forEach(q -> questionRepository.delete(q, id));
    }

    private List<Integer> getDeletedVariants(Quest quest, int id) {
        // getting posted variants
        List<Integer> requestVariants = CollectionUtils.emptyIfNull(quest.getQuestions()).stream()
                .filter(q -> q.getVariants() != null)
                .flatMap(q -> q.getVariants().stream())
                .filter(v -> v.getId() != null)
                .map(Variant::getId)
                .collect(Collectors.toList());

        // getting deleted variants
        List<Integer> deletedVariants = CollectionUtils.emptyIfNull(get(id).getQuestions()).stream()
                .filter(q -> q.getVariants() != null)
                .flatMap(q -> q.getVariants().stream())
                .filter(v -> !requestVariants.contains(v.getId()))
                .map(Variant::getId)
                .collect(Collectors.toList());
        return deletedVariants;
    }

    public void deleteOrphanedVariants(List<Integer> variantsToDelete) {
        variantsToDelete.stream()
                .filter(v ->  variantRepository.findById(v).orElse(null).getQuestions().size() == 0)
                .forEach(v -> variantRepository.delete(v));
    }


    @Override
    @Transactional
    public Quest save(Quest quest) {
        Assert.notNull(quest, "quest must not be null");
        return questRepository.save(prepareForPersist(quest));
    }

    @Override
    @Transactional
    public Quest update(Quest quest, int id) {
        Assert.notNull(quest, "quest must not be null");
        Quest preparedQuest = prepareForPersist(quest);
        List<Integer> deletedQuestions = getDeletedQuestions(quest, id);
        List<Integer> deletedVariants = getDeletedVariants(quest, id);
        Quest savedQuest = checkNotFoundWithId(questRepository.save(preparedQuest), id);
        deleteQuestions(deletedQuestions, id);
        deleteOrphanedVariants(deletedVariants);
        return savedQuest;
    }

    @Override
    public void delete(int id) throws NotFoundException {
        Assert.notNull(id, "quest must not be null");
        checkNotFoundWithId(questRepository.delete(id)!= 0, id);
    }

    @Override
    @Transactional
    public void deleteEntireQuest(int id) throws NotFoundException {
        Quest quest = get(id);
        if (quest.getQuestions() != null) {
            List<Integer> variantsToDelete = quest.getQuestions().stream()
                    .filter(q -> q.getVariants() != null)
                    .flatMap(q -> q.getVariants().stream())
                    .map(Variant::getId)
                    .collect(Collectors.toList());

            get(id).getQuestions().stream()
                    .forEach(q -> questionRepository.delete(q.getId(), id));

            variantsToDelete.stream()
                    .filter(v -> variantRepository.findById(v).orElse(null).getQuestions() == null)
                    .forEach(v -> variantRepository.delete(v));
        }
        delete(id);
    }

    @Override
    @Transactional
    public void enable(int id, boolean isActive) {
        Quest quest = get(id);
        quest.setActive(isActive);
        questRepository.save(quest);
    }


}
