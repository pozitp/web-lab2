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

val servletApiVersion = "5.0.0"
val jspApiVersion = "3.1.1"
val ejbApiVersion = "4.0.1"
val jstlVersion = "3.0.1"
val junitVersion = "5.10.0"

dependencies {
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    providedCompile("jakarta.servlet:jakarta.servlet-api:$servletApiVersion")
    providedCompile("jakarta.servlet.jsp:jakarta.servlet.jsp-api:$jspApiVersion")
    providedCompile("jakarta.ejb:jakarta.ejb-api:$ejbApiVersion")
    implementation("org.glassfish.web:jakarta.servlet.jsp.jstl:$jstlVersion")
}

tasks.test {
    useJUnitPlatform()
}

tasks.war {
    archiveFileName.set("web-lab2.war")
}
