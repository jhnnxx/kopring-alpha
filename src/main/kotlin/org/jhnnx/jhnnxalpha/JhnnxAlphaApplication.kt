package org.jhnnx.jhnnxalpha

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class JhnnxAlphaApplication

fun main(args: Array<String>) {
    val dotenv = Dotenv.configure().load()

    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    runApplication<JhnnxAlphaApplication>(*args)
}
