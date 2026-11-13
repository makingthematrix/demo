package com.example;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Control for WelcomeTest.clientOnInjectVarLosesItsUrl: see goal-and-requirements.md, Tech Notes / Tests.
@MicronautTest
class WelcomeJavaTest {

    @Inject
    @Client("/")
    HttpClient goodClient;

    @Test
    void clientAsInjectVar() {
        HttpResponse<String> response =
            goodClient
                .toBlocking()
                .exchange(HttpRequest.GET("/"), String.class);

        assertEquals(HttpStatus.OK, response.getStatus());
    }
}
