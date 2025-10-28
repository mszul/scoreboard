package com.testproject.scoreboard.storage;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T extends Storable<ID>, ID> {

    void save(T object);

    Optional<T> findById(ID id);

    void removeById(ID id);

    Collection<T> getAll();

    Collection<T> findByPredicate(Predicate<T> predicate);
}
