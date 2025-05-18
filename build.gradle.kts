plugins {
    id("java")
    kotlin("jvm") version "2.1.20"
}

group = "org.intense"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:2.1.20")
    // https://mvnrepository.com/artifact/com.facebook/ktfmt
    implementation("com.facebook:ktfmt:0.54")
}

tasks.test {
    useJUnitPlatform()
}