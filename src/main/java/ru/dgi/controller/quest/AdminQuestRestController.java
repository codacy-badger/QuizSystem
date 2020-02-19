package ru.dgi.controller.quest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Quest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AdminQuestRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminQuestRestController extends AbstractQuestController {
    public static final String REST_URL = "/rest/admin/quests";

    @Override
    @GetMapping(value = "/{id}")
    public Quest get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<Quest> getAll() {
        return super.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Quest updateOrCreate(@Valid @RequestBody Quest quest) {
        if (quest.isNew()) {
            return super.create(quest);
        } else {
            return super.update(quest, quest.getId());
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @DeleteMapping(value = "/entire/{id}")
    public void deleteEntireQuest(@PathVariable("id") int id) {
        super.deleteEntireQuest(id);
    }

    @Override
    @PostMapping(value = "/{id}")
    public void enable(@PathVariable("id") int id, @RequestParam("isActive") boolean isActive) {
        super.enable(id, isActive);
    }
}
