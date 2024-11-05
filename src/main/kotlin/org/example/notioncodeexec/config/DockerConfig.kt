package org.example.notioncodeexec.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("application.docker")
class DockerConfig(val image: String)