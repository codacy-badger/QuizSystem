package ru.dgi.controller.question;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Question;
import java.util.List;

@RestController
@RequestMapping(value = QuestionRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class QuestionRestController extends AbstractQuestionController {
    static final String REST_URL = "/rest/profile/quests/{questId}/questions";

    @Override
    @GetMapping(value = "/{id}")
    public Question getActive(@PathVariable("id") int id, @PathVariable("questId") int questId) {
        return super.getActive(id, questId);
    }

    @Override
    @GetMapping
    public List<Question> getAllActive(@PathVariable("questId") int questId) {
        return super.getAllActive(questId);
    }
}
