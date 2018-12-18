package org.paolo.socialn.exception;

public class FollowException extends RuntimeException {

    private final String message;

    public FollowException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String getLocalizedMessage() {
        return message;
    }
}
