import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime

plugins {
    this.kotlin("jvm") version "1.6.10"
    this.`java-library`
    this.id("com.github.johnrengelman.shadow") version "7.1.2"
    this.id("io.papermc.paperweight.userdev") version "1.3.4"
    this.id("net.kyori.blossom") version "1.2.0"
    this.id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
    this.id("net.minecrell.plugin-yml.bukkit") version "0.5.1" // Generates plugin.yml

}

group = "org.example"
version = "1.0-SNAPSHOT"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    this.mavenCentral()
    this.maven(url = "https://jitpack.io")
    this.maven(url = "https://maven.enginehub.org/repo/")
    this.maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    this.maven(url = "https://libraries.minecraft.net/")
    this.maven(url = "https://repo.incendo.org/content/repositories/snapshots")
    this.maven(url = "https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    this.paperDevBundle("1.18.2-R0.1-SNAPSHOT")
    this.implementation(this.kotlin("stdlib"))
    this.testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    this.testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    this.implementation("org.spongepowered:configurate-gson:4.1.2")
    this.implementation("com.github.ben-manes.caffeine:caffeine:3.0.5")
    this.implementation("me.lucko:commodore:1.11")
    this.implementation("net.kyori:adventure-platform-bukkit:4.0.1")
    this.compileOnly("org.jetbrains:annotations:22.0.0")
    this.compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8")
}

tasks {
    this.assemble {
        this.dependsOn(this@tasks.reobfJar)
    }

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

    this.compileKotlin {
        this.kotlinOptions {
            this.jvmTarget = "17"
            this.freeCompilerArgs = listOf("-Xjvm-default=enable")
        }
    }

    this.test {
        this.useJUnitPlatform()
    }

    this.shadowJar {
        this.relocate("kotlin","me.conclure.eventful.kotlin")
    }
}

blossom {
    this.replaceToken("\${COMPILE_TIME}", LocalDateTime.now().toString())
}

bukkit {
    this.main = "me.conclure.eventful.bootstrap.EventfulJavaPlugin"
    this.apiVersion = "1.18"
    this.authors = listOf("Conclure")
    this.commands {
        this.create("event")
        this.create("eventstart")
        this.create("eventcreate")
        this.create("lobby")
        this.create("setspawn")
        this.create("spawn")
    }
}