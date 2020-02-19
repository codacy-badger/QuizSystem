package ru.dgi.service;

import ru.dgi.util.exception.NotFoundException;
import ru.dgi.model.Variant;
import java.util.List;

public interface VariantService {

    Variant get(int id) throws NotFoundException;

    Variant getByName(String name);

    List<Variant> getAll();

    Variant save(Variant variant);

    Variant update(Variant variant, int id) throws NotFoundException;

    void delete(int id) throws NotFoundException;

    List<String> getTagsByQuery(String query);
}
