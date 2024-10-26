package org.example.notioncodeexec.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("docker")
class DockerConfig(val image: String)