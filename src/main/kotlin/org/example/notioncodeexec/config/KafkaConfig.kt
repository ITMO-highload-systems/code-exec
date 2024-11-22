package org.example.notioncodeexec.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaConfig {

    @Bean
    fun topic2() =
        TopicBuilder.name("executions")
            .partitions(2)
            .replicas(3)
            .build()
}