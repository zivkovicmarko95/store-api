package com.store.storesharedmodule.utils;

import com.store.storesharedmodule.models.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {
    
    private HttpUtils() {

    }

    public static ResponseEntity<HttpResponse> createHttpResponse(final HttpStatus httpStatus, final String message) {
        ArgumentVerifier.verifyNotNull(httpStatus, message);

        final HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message);

        return new ResponseEntity<>(httpResponse, httpStatus);
    }

}
