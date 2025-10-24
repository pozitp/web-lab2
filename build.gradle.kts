plugins {
    id("java")
    id("war")
}

val providedCompile: Configuration by configurations

group = "ru.pozitp"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    providedCompile("jakarta.servlet:jakarta.servlet-api:5.0.0")
    providedCompile("jakarta.servlet.jsp:jakarta.servlet.jsp-api:3.1.1")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:3.0.1")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("web-lab2.war")
}
