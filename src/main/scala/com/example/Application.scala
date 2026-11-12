package com.example

import io.micronaut.runtime.Micronaut

object Application {
  def main(args: Array[String]): Unit =
    Micronaut.run(args*)
}
