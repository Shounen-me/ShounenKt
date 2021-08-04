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
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.manuelkour"
            artifactId = "Shounen4J"
            version = "0.1"

            from(components["java"])
        }
    }
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}