plugins {
	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("jvm") version "2.0.0"
	kotlin("plugin.spring") version "2.0.0"
}

group = "com.yugyeong"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("javax.xml.bind:jaxb-api:2.3.1")
	implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")

	implementation("com.amazonaws:aws-java-sdk-s3:1.12.300")
	implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")

	implementation("dev.langchain4j:langchain4j:0.32.0")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("dev.langchain4j:langchain4j-open-ai:0.32.0")
	implementation("dev.langchain4j:langchain4j-chroma:0.32.0")
	implementation("dev.langchain4j:langchain4j-spring-boot-starter:0.32.0")
	implementation("dev.langchain4j:langchain4j-open-ai-spring-boot-starter:0.32.0")
	implementation("dev.langchain4j:langchain4j-easy-rag:0.32.0")
	implementation("org.testcontainers:testcontainers")
	implementation("org.testcontainers:chromadb")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict") // 어노테이션의 nullability 정보를 엄격하게 처리
	}
}

sourceSets {
	main {
		java {
			setSrcDirs(listOf("src/main/java", "src/main/kotlin"))
		}
	}
	test {
		java {
			setSrcDirs(listOf("src/test/java", "src/test/kotlin"))
		}
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
