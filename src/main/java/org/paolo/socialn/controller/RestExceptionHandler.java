package org.paolo.socialn.controller;

import org.paolo.socialn.exception.FollowException;
import org.paolo.socialn.exception.UserNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ UserNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, String.format("Username %s not found", ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ FollowException.class, ConstraintViolationException.class, DataIntegrityViolationException.class, TransactionSystemException.class})
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {

        if (ex instanceof TransactionSystemException) {
            Throwable e = ((TransactionSystemException)ex).getOriginalException();

            if(e instanceof RollbackException) {

                RollbackException rex = (RollbackException) e;

                if(rex.getCause() instanceof ConstraintViolationException) {

                    ConstraintViolationException cex = (ConstraintViolationException) rex.getCause();

                    StringBuilder sb = new StringBuilder();
                    cex.getConstraintViolations().forEach(constraintViolation -> sb.append(constraintViolation.getMessage()).append('\n'));

                    return handleExceptionInternal(cex, sb.toString(),
                            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
                }
            }
        }

        return handleExceptionInternal(ex, ex.getLocalizedMessage(),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
