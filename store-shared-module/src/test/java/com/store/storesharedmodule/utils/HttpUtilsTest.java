package com.store.storesharedmodule.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.store.storesharedmodule.models.HttpResponse;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

class HttpUtilsTest {
    
    private final static PodamFactory PODAM_FACTORY = new PodamFactoryImpl();

    @Test
    void createHttpResponse() {

        final HttpStatus httpStatus = PODAM_FACTORY.manufacturePojo(HttpStatus.class);
        final String msg = PODAM_FACTORY.manufacturePojo(String.class);
         
        final ResponseEntity<HttpResponse> response = HttpUtils.createHttpResponse(httpStatus, msg);

        assertThat(response.getStatusCode()).isEqualTo(httpStatus);
        assertThat(response.getBody().getHttpStatus()).isEqualTo(httpStatus);
        assertThat(response.getBody().getMessage()).isEqualTo(msg);
        assertThat(response.getBody().getHttpStatusCode()).isEqualTo(httpStatus.value());
        assertThat(response.getBody().getReason()).isEqualTo(httpStatus.getReasonPhrase());
    }

}
