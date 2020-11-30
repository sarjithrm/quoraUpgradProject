package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    /*
     *Exception handler for SignUpRestrictedException
     *HttpStatus: UNPROCESSABLE_ENTITY
     *@Param SignUpRestrictedException, WebRequest
     *@return ResponseEntity<ErrorResponse> with error code and message
     */
    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signInRestrictedException(SignUpRestrictedException exp, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exp.getCode()).message(exp.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /*
     *Exception handler for AuthenticationFailedException
     *HttpStatus: UNAUTHORIZED
     *@Param AuthenticationFailedException, WebRequest
     *@return ResponseEntity<ErrorResponse> with error code and message
     */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException exp, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exp.getCode()).message(exp.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /*
     *Exception handler for SignOutRestrictedException
     *HttpStatus: UNAUTHORIZED
     *@Param SignOutRestrictedException, WebRequest
     *@return ResponseEntity<ErrorResponse> with error code and message
     */
    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> signOutRestrictedException(SignOutRestrictedException exp, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exp.getCode()).message(exp.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /*
     *Exception handler for AuthorizationFailedException
     *HttpStatus: UNAUTHORIZED
     *@Param AuthorizationFailedException, WebRequest
     *@return ResponseEntity<ErrorResponse> with error code and message
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authoriationFailedException(AuthorizationFailedException exp, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exp.getCode()).message(exp.getErrorMessage()), HttpStatus.UNAUTHORIZED);
    }

    /*
     *Exception handler for UserNotFoundException
     *HttpStatus: UNPROCESSED_ENTITY
     *@Param UserNotFoundException, WebRequest
     *@return ResponseEntity<ErrorResponse> with error code and message
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException exp, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exp.getCode()).message(exp.getErrorMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
