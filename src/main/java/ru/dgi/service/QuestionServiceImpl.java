package ru.dgi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.dgi.dao.QuestionRepository;
import ru.dgi.dao.VariantRepository;
import ru.dgi.to.QuestionStatistics;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Question;
import java.util.ArrayList;
import java.util.List;

import static ru.dgi.util.ValidationUtil.checkNotFoundWithId;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public Question get(int id, int questId) throws NotFoundException {
        Question question = questionRepository.findById(id).orElse(null);
        return checkNotFoundWithId(question != null && question.getQuest().getId() == questId ? question : null, id);
    }

    @Override
    public List<Question> getAll(int questId) {
        return questionRepository.getAll(questId);
    }

    @Override
    public Question getActive(int id, int questId) throws NotFoundException {
        Question question = questionRepository.getActive(id, questId);
        return checkNotFoundWithId(question != null && question.getQuest().getId() == questId ? question : null, id);
    }

    @Override
    public List<Question> getAllActive(int questId) {
        return questionRepository.getAllActive(questId);
    }

    @Override
    public List<QuestionStatistics> getQuestionStatistics(int questId) {
        List<Question> questions = questionRepository.getAll(questId);
        List<QuestionStatistics> questionStatistics = new ArrayList<>();
        for (Question question : questions) {
            questionStatistics.add(new QuestionStatistics(question.getId(),
                    question.getName(), question.getNumber(), question.getAnswerTypeId(),
                    variantRepository.getVariantStatistics(question.getId(), questId)));
        }
        return questionStatistics;
    }

    @Override
    public Question save(Question question) {
        Assert.notNull(question, "question must not be null");
        return questionRepository.save(question);
    }

    @Override
    public Question update(Question question, int id) throws NotFoundException {
        Assert.notNull(question, "question must not be null");
        return checkNotFoundWithId(questionRepository.save(question),id);
    }

    @Override
    public void delete(int id, int questId) throws NotFoundException {
        Assert.notNull(id, "question must not be null");
        checkNotFoundWithId(questionRepository.delete(id, questId) != 0, id);
    }
}
