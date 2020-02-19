package ru.dgi.service;

import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Result;
import java.util.List;

public interface ResultService {

    Result get(int id, int questId) throws NotFoundException;

    Result getResultByQuestIdAndUsername(int questId, String username);

    List<Result> getAll(int questId);

    long getCount(int questId);

    Result save(Result result);

    Result update(Result result, int id) throws NotFoundException;

    void delete(int id, int questId) throws NotFoundException;
}