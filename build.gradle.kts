import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("org.asciidoctor.convert") version "1.5.8"
    id("com.google.cloud.tools.jib") version "2.4.0"
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
    kotlin("plugin.jpa") version "1.4.10"
    jacoco
    idea
}

group = "yieon.kakaopay.workplatform"
version = project.properties["version"] ?: "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_11


repositories {
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")

    jacoco {
        // JaCoCo
        toolVersion = "0.8.7"
    }

    val snippetsDir by extra { file("build/generated-snippets") }
    val querydslVer = "4.3.1"
    val kotlinVer = "1.3.72"
    val mockitoKotlinVer = "2.2.0"
    val mockitoInlineVer = "2.21.0"
    val jjwtVer = "0.11.2"
    val jsoupVer = "1.13.1"
    val springCloudVersion = "Hoxton.SR5"

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}")
        }
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-security")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
        implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

        api("com.querydsl:querydsl-jpa:$querydslVer")
        kapt("com.querydsl:querydsl-apt:$querydslVer:jpa")

        implementation("io.jsonwebtoken:jjwt-api:$jjwtVer")
        implementation("io.jsonwebtoken:jjwt-impl:$jjwtVer")
        implementation("io.jsonwebtoken:jjwt-jackson:$jjwtVer")
        implementation("com.squareup.okhttp3:okhttp:4.9.0")

        implementation("org.jsoup:jsoup:$jsoupVer")
        implementation("com.vladmihalcea:hibernate-types-52:2.10.4")
        implementation("org.bouncycastle:bcpkix-jdk15on:1.50")

        developmentOnly("org.springframework.boot:spring-boot-devtools")

        testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
        testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
        testImplementation("com.squareup.okhttp3:mockwebserver:4.8.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
        testImplementation("io.projectreactor:reactor-test")
        testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVer")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlinVer")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVer")
        testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:$mockitoKotlinVer")
        testImplementation("org.mockito:mockito-inline:$mockitoInlineVer")
        asciidoctor("org.springframework.restdocs:spring-restdocs-asciidoctor:2.0.1.RELEASE")
        implementation("org.apache.logging.log4j:log4j-api:2.17.2")
        implementation("org.apache.logging.log4j:log4j-to-slf4j:2.17.2")
        implementation("ch.qos.logback:logback-classic:1.2.11")
        implementation("ch.qos.logback:logback-core:1.2.11")

        implementation("com.h2database:h2")
        implementation("org.liquibase:liquibase-core")
    }


    fun getProfile(): String {
        return if (project.hasProperty("profile")) {
            print(project.property("profile"))
            project.property("profile").toString()
        } else {
            "local"
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    tasks.test {
        useJUnitPlatform()
        systemProperty("spring.profiles.active", getProfile())
        outputs.dir(snippetsDir)
    }

    tasks.asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(tasks.test)
        doFirst {
            println("=====start asciidoctor")
            delete {
                file("build/resources/main/static/${project.name}")
            }
        }
        doLast {
            println("=====finish asciidoctor")
            copy {
                from("build/asciidoc/html5")
                into("src/main/resources/static/docs")
            }
        }
    }

    tasks.jacocoTestReport {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
            csv.isEnabled = false
        }

        finalizedBy("jacocoTestCoverageVerification")
    }

    tasks.jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    minimum = "0.30".toBigDecimal()
                }
            }
            rule {
                enabled = true
                element = "CLASS"
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.30".toBigDecimal()
                }
                excludes = listOf(
                        "yieon.kakaopay.workplatform.InquiryApplication",
                        "*.config.*",
                        "*.entity.*",
                        "*.model.*",
                        "*.util.*"
                )
            }
        }
    }

    idea {
        module {
            val kaptMain = file("build/generated/source/kapt/main")
            sourceDirs.add(kaptMain)
            generatedSourceDirs.add(kaptMain)
        }
    }
}

tasks.jibDockerBuild {
    dependsOn(tasks.asciidoctor)
}

springBoot {
    buildInfo()
}
