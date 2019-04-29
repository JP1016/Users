package com.store.user.exception;

import com.store.user.model.response.ErrorResponse;
import com.store.user.utils.AppConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(UserCreationFailedException.class)
    public final ResponseEntity<ErrorResponse> failedRequest(UserCreationFailedException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), AppConstants.USER_CREATION_FAILED,AppConstants.SERVER_ERROR);
        return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleAllExceptions(UserNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), AppConstants.USER_NOT_FOUND,AppConstants.NOT_FOUND);
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

}
