package ru.erminson.lc.model.exception;

public class IllegalInitialDataException extends Exception {
    public IllegalInitialDataException() {
        super();
    }

    public IllegalInitialDataException(String message) {
        super(message);
    }

    public IllegalInitialDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInitialDataException(Throwable cause) {
        super(cause);
    }
}
