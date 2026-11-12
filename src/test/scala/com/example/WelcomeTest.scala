package com.example

import io.micronaut.http.{HttpRequest, HttpStatus, MediaType}
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.NoHostException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.{assertEquals, assertThrows, assertTrue}
import org.junit.jupiter.api.Test

import scala.compiletime.uninitialized

@MicronautTest
class WelcomeTest {
	// Plugin bug: see goal-and-requirements.md, Tech Notes / Tests.
	@Inject
	@Client("/")
	var badClient: HttpClient = uninitialized

  @Test
  def clientOnInjectVarLosesItsUrl(): Unit = {
    val error = assertThrows(
      classOf[NoHostException],
      () => {
	      badClient
		      .toBlocking
		      .exchange(HttpRequest.GET[Any]("/"), classOf[String])
	      ()
      }
    )

    assertEquals("Request URI specifies no host to connect to", error.getMessage)
  }

	@Test
	def clientAsParameterKeepsItsUrl(@Client("/") client: HttpClient): Unit = {
		val response = client.toBlocking.exchange(HttpRequest.GET[Any]("/"), classOf[String])

		assertEquals(HttpStatus.OK, response.getStatus)
		assertEquals(MediaType.TEXT_HTML_TYPE, response.getContentType.orElseThrow())
		val body = response.body()
		assertTrue(body.contains("<title>Welcome</title>"), body)
		assertTrue(body.contains("<h1>Welcome!</h1>"), body)
	}

}
