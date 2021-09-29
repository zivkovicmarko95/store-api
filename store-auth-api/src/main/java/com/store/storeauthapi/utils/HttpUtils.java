package com.store.storeauthapi.utils;

import com.store.storeauthapi.models.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpUtils {
    
    private HttpUtils() {

    }

    public static ResponseEntity<HttpResponse> createHttpResponse(final HttpStatus httpStatus, final String message) {
        // TODO: add argument verifier - verify not null

        final HttpResponse httpResponse = new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase(), message);

        return new ResponseEntity<>(httpResponse, httpStatus);
    }

}
