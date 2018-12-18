package org.paolo.socialn.exception;

public class UserNotFoundException extends RuntimeException {

    private final String userName;

    public UserNotFoundException(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return userName;
    }
}
