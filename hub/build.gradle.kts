plugins {
    this.id("xyz.jpenilla.run-paper")
    this.id("net.minecrell.plugin-yml.bukkit")
    this.id("io.papermc.paperweight.userdev")
}

dependencies {
    this.paperDevBundle("1.18.2-R0.1-SNAPSHOT")

    this.implementation(this.project(":common"))
    this.implementation("me.lucko:commodore:1.11")
    this.implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    this.compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8")
}

tasks {
    this.assemble {
        this.dependsOn(this@tasks.reobfJar)
    }
}

bukkit {
    this.main = "me.conclure.eventful.hub.EventfulHubPlugin"
    this.apiVersion = "1.18"
    this.authors = listOf("Conclure")
    this.permissions {
    }
    this.commands {
        this.create("ping")
    }
}