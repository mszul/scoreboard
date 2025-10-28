package com.testproject.scoreboard.storage;

import java.util.*;
import java.util.function.Predicate;

public class InMemoryCollectionRepository<T extends Storable<ID>, ID> implements Repository<T, ID> {

    private final Map<ID, T> objects;

    public InMemoryCollectionRepository() {
        objects = new HashMap<>();
    }

    @Override
    public void save(T object) {
        objects.put(object.getId(), object);
    }

    @Override
    public void removeById(ID id) {
        objects.remove(id);
    }

    @Override
    public Collection<T> getAll() {
        return objects.values();
    }

    @Override
    public List<T> findByPredicate(Predicate<T> predicate) {
        return objects.values().stream().filter(predicate).toList();
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(objects.get(id));
    }
}
