package com.store.storemanagementapi.exceptions.handlers;

import java.security.InvalidParameterException;
import java.util.Objects;

import com.store.storemanagementapi.exceptions.InvalidResourceStateException;
import com.store.storemanagementapi.exceptions.ParameterNotSupportedException;
import com.store.storemanagementapi.exceptions.ResourceExistException;
import com.store.storemanagementapi.exceptions.ResourceNotFoundException;
import com.store.storesharedmodule.models.HttpResponse;
import com.store.storesharedmodule.utils.HttpUtils;

import org.apache.commons.lang3.exception.ExceptionUtils;
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
public class GlobalExceptionHandler implements ErrorController {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private static final String METHOD_IS_NOT_ALLOWED_MESSAGE = "This method is not supported. Please send a '%s' request";
    private static final String RESOURCE_EXIST_MESSAGE = "It's not possible to do any action with provided parameter. Resource exist in the database.";
    private static final String PARAMETER_NOT_SUPPORTED_MESSAGE = "Provided parameter is not supported.";
    private static final String RESOURCE_NOT_FOUND_MESSAGE = "Resource is not found.";
    private static final String HTTP_MESSAGE_NOT_READABLE = "Provided HTTP message is not readable.";
    private static final String GENERAL_EXCEPTION_MESSAGE = "Server is unavailbale now. Try again later.";
    private static final String INVALID_RESOURCE_STATE_MESSAGE = "Invalid resource state.";

    private final String ERROR_PATH = "api/store/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<HttpResponse> invalidParameters(InvalidParameterException e) {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ParameterNotSupportedException.class)
    public ResponseEntity<HttpResponse> invalidParameters(ParameterNotSupportedException e) {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, PARAMETER_NOT_SUPPORTED_MESSAGE);
    }

    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<HttpResponse> resourceExist() {
        return HttpUtils.createHttpResponse(HttpStatus.CONFLICT, RESOURCE_EXIST_MESSAGE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpResponse> resourceNotFound() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, RESOURCE_NOT_FOUND_MESSAGE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpResponse> messageNotReadableException() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, HTTP_MESSAGE_NOT_READABLE);
    }

    @ExceptionHandler(InvalidResourceStateException.class)
    public ResponseEntity<HttpResponse> invalidResourceStateException() {
        return HttpUtils.createHttpResponse(HttpStatus.BAD_REQUEST, INVALID_RESOURCE_STATE_MESSAGE);
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerError(Exception e) {
        LOGGER.error(ExceptionUtils.getStackTrace(e));
        
        return HttpUtils.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, GENERAL_EXCEPTION_MESSAGE);
    }
}
