package ru.dgi.controller.variant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Variant;
import java.util.List;

@RestController
@RequestMapping(value = VariantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VariantRestController extends AbstractVatriantController {
    static final String REST_URL = "/rest/profile/variants";

    @Override
    @GetMapping(value = "/{id}")
    public Variant get(@PathVariable("id") int id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<Variant> getAll() {
        return super.getAll();
    }
}
