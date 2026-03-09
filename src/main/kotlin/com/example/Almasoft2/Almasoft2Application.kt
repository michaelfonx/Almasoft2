package com.example.Almasoft2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.example"])
class Almasoft2Application

fun main(args: Array<String>) {

	runApplication<Almasoft2Application>(*args)
}
