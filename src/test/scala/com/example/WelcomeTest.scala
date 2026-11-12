package com.example

import io.micronaut.http.{HttpRequest, HttpStatus, MediaType}
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.{assertEquals, assertTrue}
import org.junit.jupiter.api.Test

@MicronautTest
class WelcomeTest:

  @Test
  def rootServesTheWelcomePage(@Client("/") client: HttpClient): Unit =
    val response = client.toBlocking.exchange(HttpRequest.GET[Any]("/"), classOf[String])

    assertEquals(HttpStatus.OK, response.getStatus)
    assertEquals(MediaType.TEXT_HTML_TYPE, response.getContentType.orElseThrow())
    val body = response.body()
    assertTrue(body.contains("<title>Welcome</title>"), body)
    assertTrue(body.contains("<h1>Welcome!</h1>"), body)
