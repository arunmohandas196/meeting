package com.akqa.meeting.console.exceptions;

public class ConsoleException extends RuntimeException {
    public ConsoleException() {
    }

    public ConsoleException(String message) {
        super(message);
    }

    public ConsoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
