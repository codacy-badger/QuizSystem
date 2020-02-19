package ru.dgi.controller.result;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Result;
import javax.validation.Valid;

@RestController
@RequestMapping(value = ResultRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ResultRestController extends AbstractResultController {
    static final String REST_URL = "/rest/profile/quests/{questId}/results";

    @Override
    @GetMapping
    public Result getResultByQuestIdAndUsername(@PathVariable("questId") int questId) {
        return super.getResultByQuestIdAndUsername(questId);
    }

    @Override
    @PostMapping(value = "/entire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result saveEntireResult(@Valid @RequestBody Result result) {
        return super.saveEntireResult(result);
    }
}
