package ru.erminson.lc.model.exception;

public class EntityHasAlreadyBeenCreated extends EntityException {
    public EntityHasAlreadyBeenCreated(Class<?> clazz, String... params) {
        super(clazz, " has already been created ", params);
    }
}
