package ru.dgi.controller.question;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Question;
import ru.dgi.to.QuestionStatistics;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AdminQuestionRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminQuestionRestController extends AbstractQuestionController {
    static final String REST_URL = "/rest/admin/quests/{questId}/questions";

    @Override
    @GetMapping(value = "/{id}")
    public Question get(@PathVariable("id") int id, @PathVariable("questId") int questId) {
        return super.get(id, questId);
    }

    @Override
    @GetMapping
    public List<Question> getAll(@PathVariable("questId") int questId) {
        return super.getAll(questId);
    }

    @Override
    @GetMapping(value = "/statistics")
    public List<QuestionStatistics> getQuestionStatistics(@PathVariable("questId") int questId) {
        return super.getQuestionStatistics(questId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Question updateOrCreate(@Valid @RequestBody Question question) {
        if (question.isNew()) {
            return super.create(question);
        } else {
            return super.update(question, question.getId());
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable("questId") int questId) {
        super.delete(id, questId);
    }
}
