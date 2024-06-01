package com.knarusawa.webauthndemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebauthnDemoApplication

fun main(args: Array<String>) {
  runApplication<WebauthnDemoApplication>(*args)
}
