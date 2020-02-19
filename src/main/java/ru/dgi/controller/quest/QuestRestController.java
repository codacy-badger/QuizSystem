package ru.dgi.controller.quest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Quest;
import ru.dgi.to.QuestWithResult;
import java.util.List;

@RestController
@RequestMapping(value = QuestRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestRestController extends AbstractQuestController {
    public static final String REST_URL = "/rest/profile/quests";

    @Override
    @GetMapping(value = "/{id}")
    public Quest getActive(@PathVariable("id") int id) {
        return super.getActive(id);
    }

    @Override
    @GetMapping
    public List<QuestWithResult> getAllActiveOrStarted() {
        return super.getAllActiveOrStarted();
    }
}
