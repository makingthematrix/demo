package com.example

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.{Controller, Get}

@Controller("/")
class WelcomeController:

  @Get(produces = Array(MediaType.TEXT_HTML))
  def index(): String = WelcomeController.Page

object WelcomeController:
  val Page: String =
    """<!DOCTYPE html>
      |<html lang="en">
      |<head>
      |  <meta charset="utf-8">
      |  <title>Welcome</title>
      |</head>
      |<body>
      |  <h1>Welcome!</h1>
      |</body>
      |</html>
      |""".stripMargin
