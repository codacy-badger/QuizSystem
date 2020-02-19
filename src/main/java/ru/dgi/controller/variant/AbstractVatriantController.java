package ru.dgi.controller.variant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.dgi.model.Variant;
import ru.dgi.service.VariantService;
import java.util.List;
import static ru.dgi.util.ValidationUtil.*;

public class AbstractVatriantController {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private VariantService variantService;

    public Variant get(int id) {
        LOG.info("get " + id);
        return variantService.get(id);
    }

    public List<Variant> getAll() {
        LOG.info("getAll");
        return variantService.getAll();
    }

    public Variant create(Variant variant) {
        LOG.info("create " + variant);
        checkNew(variant);
        return variantService.save(variant);
    }

    public Variant update(Variant variant, int id) {
        LOG.info("update " + variant);
        assureIdConsistent(variant, id);
        return variantService.update(variant, id);
    }

    public void delete(int id) {
        LOG.info("delete " + id);
        variantService.delete(id);
    }

    public List<String> getTagsByQuery(String query) {
        LOG.info("tags by query " + query);
        return variantService.getTagsByQuery(query);
    }
}
