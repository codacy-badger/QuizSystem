package ru.dgi.controller.answer;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dgi.to.AnswerTo;

@RestController
@RequestMapping(value = AdminAnswerRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminAnswerRestController extends AbstractAnswerController {
    static final String REST_URL = "/rest/admin/questions/{questionId}/answers";

    @Override
    @GetMapping(value = "/getonemorethanid/{id}")
    public AnswerTo getOneMoreThanId(@PathVariable("id") int id, @PathVariable("questionId") int questionId) {
        return super.getOneMoreThanId(id, questionId);
    }
}
