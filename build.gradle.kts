import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime

plugins {
    this.`java-library`
    this.id("com.github.johnrengelman.shadow") version "7.1.2"
    this.id("io.papermc.paperweight.userdev") version "1.3.4" apply false
    this.id("net.kyori.blossom") version "1.2.0"
    this.id("xyz.jpenilla.run-paper") version "1.0.6" apply false
    this.id("net.minecrell.plugin-yml.bukkit") version "0.5.1"

}

group = "org.example"
version = "1.0-SNAPSHOT"

subprojects {
    this.apply(plugin = "java-library")
    this.apply(plugin = "net.kyori.blossom")
    this.apply(plugin = "com.github.johnrengelman.shadow")

    this.java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    this.repositories {
        this.mavenCentral()

        this.maven(url = "https://jitpack.io")
        this.maven(url = "https://maven.enginehub.org/repo/")
        this.maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        this.maven(url = "https://libraries.minecraft.net/")
        this.maven(url = "https://repo.incendo.org/content/repositories/snapshots")
        this.maven(url = "https://repo.codemc.io/repository/maven-snapshots/")
        this.maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
        this.maven(url = "https://repo.velocitypowered.com/snapshots/")
    }

    this.dependencies {
        this.compileOnly("org.jetbrains:annotations:23.0.0")

        this.testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
        this.testImplementation("org.mockito:mockito-core:4.5.1")
        this.testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }

    this.tasks {
        this.compileJava {
            this.options.encoding = Charsets.UTF_8.name()
            this.options.release.set(17)
        }
        this.javadoc {
            this.options.encoding = Charsets.UTF_8.name()
        }
        this.processResources {
            this.filteringCharset = Charsets.UTF_8.name()
        }

        this.test {
            this.useJUnitPlatform()
        }
    }

    this.blossom {
        this.replaceToken("\${COMPILE_TIME}", LocalDateTime.now().toString())
    }
}