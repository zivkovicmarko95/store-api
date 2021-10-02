package com.store.storeproductapi.exceptions.handlers;

import java.util.Objects;

import com.store.storeproductapi.exceptions.ResourceExistException;
import com.store.storeproductapi.exceptions.ResourceStateException;
import com.store.storeproductapi.exceptions.StoreGeneralException;
import com.store.storeproductapi.exceptions.StoreResourceNotFoundException;
import com.store.storesharedmodule.exceptions.StoreArgumentException;
import com.store.storesharedmodule.models.HttpResponse;
import com.store.storesharedmodule.utils.HttpUtils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler implements ErrorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final String ERROR_PATH = "api/store/error";

    private final static String RESOURCE_EXIST_MESSAGE = "It's not possible to do any action with provided parameter. Resource exist in the database.";
    private static final String RESOURCE_STATE_MESSAGE = "Resource is in invalid state.";
    private static final String METHOD_IS_NOT_ALLOWED_MESSAGE = "This method is not supported. Please send a '%s' request";
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource is not found.";
    private static final String EMPTY_OR_NULL_PARAMETER_MESSAGE = "Provided parameters contain null or empty value.";
    private static final String GENERAL_EXCEPTION_MESSAGE = "Server is unavailbale now. Try again later.";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<HttpResponse> resourceExist() {
        return HttpUtils.createHttpResponse(HttpStatus.CONFLICT, RESOURCE_EXIST_MESSAGE);
    }

    @ExceptionHandler(ResourceStateException.class)
    public ResponseEntity<HttpResponse> resourceStateException() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, RESOURCE_STATE_MESSAGE);
    }

    @ExceptionHandler(StoreResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> resourceNotFoundException(StoreResourceNotFoundException e) {
        return HttpUtils.createHttpResponse(HttpStatus.NOT_FOUND, RESOURCE_NOT_FOUND_MESSAGE + e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));

        HttpMethod supportedHttpMethods = Objects.requireNonNull(e.getSupportedHttpMethods()).stream()
                .findAny()
                .get();
                
        return HttpUtils.createHttpResponse(
                HttpStatus.METHOD_NOT_ALLOWED, 
                String.format(
                    METHOD_IS_NOT_ALLOWED_MESSAGE, 
                    supportedHttpMethods
                )
        );
    }

    @ExceptionHandler(StoreArgumentException.class)
    public ResponseEntity<HttpResponse> emptyArgument() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, EMPTY_OR_NULL_PARAMETER_MESSAGE);
    }

    @ExceptionHandler(StoreGeneralException.class)
    public ResponseEntity<HttpResponse> storeGeneralException() {
        return HttpUtils.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_EXCEPTION_MESSAGE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerError() {
        return HttpUtils.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_EXCEPTION_MESSAGE);
    }

}
