package ru.dgi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.dgi.dao.ResultRepository;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Result;
import java.util.List;
import static ru.dgi.util.ValidationUtil.checkNotFoundWithId;

@Service
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Override
    public Result get(int id, int questId) throws NotFoundException {
        Result result = resultRepository.findById(id).orElse(null);
        return checkNotFoundWithId(result != null && result.getQuest().getId() == questId ? result : null, id);
    }

    @Override
    public Result getResultByQuestIdAndUsername(int questId, String username)
    {
        return resultRepository.getResultByQuestIdAndUsername(questId, username);
    }

    @Override
    public List<Result> getAll(int questId) {
        return resultRepository.getAll(questId);
    }

    @Override
    public long getCount(int questId) {
        return resultRepository.getCount(questId);
    }

    @Override
    public Result save(Result result) {
        Assert.notNull(result, "result must not be null");
        return resultRepository.save(result);
    }

    @Override
    public Result update(Result result, int id) throws NotFoundException {
        Assert.notNull(result, "result must not be null");
        return checkNotFoundWithId(resultRepository.save(result),id);
    }

    @Override
    public void delete(int id, int questId) throws NotFoundException {
        checkNotFoundWithId(resultRepository.delete(id, questId)!= 0, id);
    }
}
