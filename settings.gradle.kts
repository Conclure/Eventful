pluginManagement {
    this.repositories {
        this.mavenCentral()
        this.gradlePluginPortal()
        this.maven("https://papermc.io/repo/repository/maven-public/")
    }
}

rootProject.name = "Eventful"

include("common","game","hub","proxy")