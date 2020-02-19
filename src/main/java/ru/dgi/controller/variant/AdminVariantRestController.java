package ru.dgi.controller.variant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.dgi.model.Variant;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = AdminVariantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminVariantRestController extends AbstractVatriantController {
    static final String REST_URL = "/rest/admin/variants";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Variant updateOrCreate(@Valid @RequestBody Variant variant) {
        if (variant.isNew()) {
            return super.create(variant);
        } else {
            return super.update(variant, variant.getId());
        }
    }

    @Override
    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @Override
    @GetMapping(value = "/tags")
    public List<String> getTagsByQuery(@RequestParam String query) {
        return super.getTagsByQuery(query);
    }
}
