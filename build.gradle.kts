import org.gradle.jvm.tasks.Jar

group = "app.shosetsu.lib"
version = "1.0.0"
description = "Kotlin library for shosetsu"

plugins {
    kotlin("jvm") version "1.3.61"
	id("org.jetbrains.dokka") version "0.10.0"
}

tasks.dokka {
	outputFormat = "html"
	outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
	group = JavaBasePlugin.DOCUMENTATION_GROUP
	description = "Assembles Kotlin docs with Dokka"
	classifier = "javadoc"
}

repositories {
    jcenter()
	mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jsoup:jsoup:1.12.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.61")
	implementation("org.luaj:luaj-jse:3.0.1")
    implementation("org.json:json:20190722")
	implementation( "com.squareup.okhttp3:okhttp:4.2.1")
	testImplementation("junit:junit:4.12")
//	implementation("org.jetbrains.kolin:kotlin-test:v1.3.61")
}

