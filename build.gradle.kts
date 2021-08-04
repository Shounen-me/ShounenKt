plugins {
    java
    `maven-publish`
}

group = "com.github.manuelkour"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    implementation("org.json:json:20090211")
    implementation ("com.github.manuelkour:Shounen4J:master-SNAPSHOT")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}