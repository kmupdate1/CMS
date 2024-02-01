val ktor_version: String by project
val logback_version: String by project
val serialization_version: String by project
val exposed_version: String by project
val postgresql_version: String by project
val javaxMail_version: String by project
val simpleKotlinMail_version: String by project
val kotlinHtml_version: String by project
val kotlinCsv_version: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

group = "wataju.jp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-auth:$ktor_version")
    implementation("io.ktor:ktor-server-mustache-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialization_version")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:$kotlinCsv_version")

    implementation("org.postgresql:postgresql:$postgresql_version")

    implementation("javax.mail:mail:$javaxMail_version")

    implementation("net.axay:simplekotlinmail-core:$simpleKotlinMail_version")
    implementation("net.axay:simplekotlinmail-client:$simpleKotlinMail_version")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinHtml_version")
    implementation("io.ktor:ktor-server-core-jvm:2.3.7")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.7")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.7")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(18)
}

