package com.store.storeauthapi.exceptions.handlers;

import com.store.storeauthapi.exceptions.StoreVerificationException;
import com.store.storesharedmodule.exceptions.StoreArgumentException;
import com.store.storesharedmodule.models.HttpResponse;
import com.store.storesharedmodule.utils.HttpUtils;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    private final String ERROR_PATH = "api/auth/error";

    private final static String INCORRECT_CREDENTIALS_MESSAGE = "Login credentials are incorrect.";
    private final static String EMPTY_OR_NULL_PARAMETER_MESSAGE = "Provided parameters contain null or empty value.";
    private static final String VERIFICATION_MESSAGE = "Verification failed";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<HttpResponse> incorrectCredentials() {
        return HttpUtils.createHttpResponse(HttpStatus.FORBIDDEN, INCORRECT_CREDENTIALS_MESSAGE);
    }

    @ExceptionHandler(StoreArgumentException.class)
    public ResponseEntity<HttpResponse> emptyArgument() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, EMPTY_OR_NULL_PARAMETER_MESSAGE);
    }

    @ExceptionHandler(StoreVerificationException.class)
    public ResponseEntity<HttpResponse> verificationFailed(final StoreVerificationException e) {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, VERIFICATION_MESSAGE +  e.getMessage());
    }

}
