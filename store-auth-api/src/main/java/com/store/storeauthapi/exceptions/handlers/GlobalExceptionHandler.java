package com.store.storeauthapi.exceptions.handlers;

import com.store.storeauthapi.models.HttpResponse;
import com.store.storeauthapi.utils.HttpUtils;

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

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ExceptionHandler(FeignException.Unauthorized.class)
    public ResponseEntity<HttpResponse> incorrectCredentials() {
        return HttpUtils.createHttpResponse(HttpStatus.FORBIDDEN, INCORRECT_CREDENTIALS_MESSAGE);
    }

}
