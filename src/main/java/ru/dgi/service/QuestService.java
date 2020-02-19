package ru.dgi.service;

import ru.dgi.to.QuestWithResult;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Quest;
import java.util.List;

public interface QuestService {

    Quest get(int id) throws NotFoundException;

    Quest getActive(int id) throws NotFoundException;

    List<Quest> getAll();

    List<QuestWithResult> getAllActiveOrStarted(String username);

    Quest save(Quest quest);

    Quest update(Quest quest, int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    void deleteEntireQuest(int id) throws NotFoundException;

    void enable(int id, boolean enable);
}
