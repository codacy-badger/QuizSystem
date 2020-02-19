package ru.dgi.controller.result;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Result;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AdminResultRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminResultRestController extends AbstractResultController {
    static final String REST_URL = "/rest/admin/quests/{questId}/results";

    @Override
    @GetMapping(value = "/{id}")
    public Result get(@PathVariable("id") int id, @PathVariable("questId") int questId) {
        return super.get(id, questId);
    }

    @Override
    @GetMapping
    public List<Result> getAll(@PathVariable("questId") int questId) {
        return super.getAll(questId);
    }

    @Override
    @GetMapping(value = "/count")
    public long getCount(@PathVariable("questId") int questId) {
        return super.getCount(questId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result updateOrCreate(@Valid @RequestBody Result result) {
        if (result.isNew()) {
            return super.create(result);
        } else {
            return super.update(result, result.getId());
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id, @PathVariable("questId") int questId) {
        super.delete(id, questId);
    }
}
