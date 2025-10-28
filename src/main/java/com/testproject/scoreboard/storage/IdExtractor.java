package com.testproject.scoreboard.storage;

@FunctionalInterface
public interface IdExtractor<T, ID> {

    ID extract(T object);
}
