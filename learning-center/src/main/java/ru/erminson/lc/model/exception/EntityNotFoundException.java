package ru.erminson.lc.model.exception;

public class EntityNotFoundException extends EntityException {
    public EntityNotFoundException(Class<?> clazz, String... params) {
        super(clazz, " was not found for parameters ", params);
    }
}
