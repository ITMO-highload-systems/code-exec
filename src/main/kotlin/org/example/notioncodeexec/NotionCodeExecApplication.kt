package org.example.notioncodeexec

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class NotionCodeExecApplication

fun main(args: Array<String>) {
    runApplication<NotionCodeExecApplication>(*args)
}
