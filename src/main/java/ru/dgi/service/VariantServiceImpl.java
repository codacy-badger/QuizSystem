package ru.dgi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.dgi.dao.VariantRepository;
import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Variant;
import java.util.List;
import static ru.dgi.util.ValidationUtil.checkNotFoundWithId;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantRepository variantRepository;

    @Override
    public Variant get(int id) throws NotFoundException {
        Variant variant = variantRepository.findById(id).orElse(null);
        return checkNotFoundWithId(variant != null ? variant : null, id);
    }

    @Override
    public Variant getByName(String name) {
        return variantRepository.getByName(name);
    }

    @Override
    public List<Variant> getAll() {
        return variantRepository.findAll();
    }

    @Override
    public Variant save(Variant variant)  {
        Assert.notNull(variant, "variant must not be null");
        return variantRepository.save(variant);
    }

    @Override
    public Variant update(Variant variant, int id) throws NotFoundException {
        Assert.notNull(variant, "variant must not be null");
        return checkNotFoundWithId(variantRepository.save(variant),id);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        Assert.notNull(id, "variant must not be null");
        checkNotFoundWithId(variantRepository.delete(id)!= 0, id);
    }

    @Override
    public List<String> getTagsByQuery(String query) {
        return variantRepository.getTagsByQuery(query);
    }
}
