plugins {
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.3"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

// Dependencies
val testContainersVersion = "1.20.1"
val jacksonVersion = "2.17.2"
val kotlinJetBrainsVersion = "2.0.20"
val reactorVersion = "3.6.11"
val postgresqlVersion = "42.7.3"
val flyWayVersion = "10.10.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.data:spring-data-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("org.flywaydb:flyway-core:$flyWayVersion")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.1")
    runtimeOnly("org.postgresql:postgresql:$postgresqlVersion")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flyWayVersion")

    // Test dependencies
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")
    testImplementation("org.testcontainers:junit-jupiter:$testContainersVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinJetBrainsVersion")
    testImplementation("org.testcontainers:postgresql:$testContainersVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.3")
    }
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

// Test task configuration
tasks.withType<Test> {
    useJUnitPlatform()
}

tasks {
    withType<JavaCompile>
    {
        dependsOn(processResources)
    }
}