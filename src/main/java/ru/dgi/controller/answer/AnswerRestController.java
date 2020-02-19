package ru.dgi.controller.answer;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Answer;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AnswerRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AnswerRestController extends AbstractAnswerController {
    static final String REST_URL = "/rest/profile/results/{resultId}/answers";

    @Override
    @GetMapping
    public List<Answer> getAll(@PathVariable("resultId") int resultId) {
        return super.getAll(resultId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Answer updateOrCreate(@Valid @RequestBody Answer answer) {
        if (answer.isNew()) {
            return super.create(answer);
        } else {
            return super.update(answer, answer.getId());
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable("resultId") int resultId) {
        super.delete(id, resultId);
    }
}
