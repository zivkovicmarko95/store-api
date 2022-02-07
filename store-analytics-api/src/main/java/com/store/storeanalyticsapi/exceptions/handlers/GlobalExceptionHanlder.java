package com.store.storeanalyticsapi.exceptions.handlers;

import java.util.Objects;

import com.store.storeanalyticsapi.exceptions.ResourceNotFoundException;
import com.store.storesharedmodule.models.HttpResponse;
import com.store.storesharedmodule.utils.HttpUtils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHanlder implements ErrorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHanlder.class);

    private final String ERROR_PATH = "api/auth/error";

    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource with provided id is not found.";
    private static final String METHOD_IS_NOT_ALLOWED_MESSAGE = "This method is not supported. Please send a '%s' request";
    private static final String HTTP_MESSAGE_NOT_READABLE = "Provided HTTP message is not readable.";
    private static final String GENERAL_EXCEPTION_MESSAGE = "Server is unavailbale now. Try again later.";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> invalidParameters(ResourceNotFoundException e) {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND_MESSAGE + e);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));

        HttpMethod supportedHttpMethods = Objects.requireNonNull(e.getSupportedHttpMethods()).stream()
                .findAny()
                .get();
                
        return HttpUtils.createHttpResponse(
                HttpStatus.METHOD_NOT_ALLOWED, 
                String.format(METHOD_IS_NOT_ALLOWED_MESSAGE, supportedHttpMethods)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponse> messageNotReadableException() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, HTTP_MESSAGE_NOT_READABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerError(Exception e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));
        
        return HttpUtils.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_EXCEPTION_MESSAGE);
    }
    
}
