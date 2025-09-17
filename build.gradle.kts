plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.flywaydb.flyway") version "10.18.0"
}

group = "org.rvsoftworks"
version = "0.0.1-SNAPSHOT"
description = "Server for Management REEM Agents"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

configurations.configureEach {
	exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
	exclude(group = "ch.qos.logback", module = "logback-classic")
}

dependencies {
	implementation(project(":REEM-Commons"))

	//Migration
	implementation ("org.flywaydb:flyway-core")
	implementation ("org.flywaydb:flyway-database-postgresql")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
//	implementation("org.springframework.boot:spring-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.github.f4b6a3:uuid-creator:6.1.0")

	//Logging
	implementation("org.springframework.boot:spring-boot-starter-log4j2")
	implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
	implementation("org.slf4j:slf4j-api:1.7.30")

	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")

	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
