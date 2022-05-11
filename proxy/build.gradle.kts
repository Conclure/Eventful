dependencies {
    this.compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    this.annotationProcessor("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    this.implementation(this.project(":common"))
}

tasks {
    val cloneJar = this.create("cloneJar") {
        this.dependsOn(this@tasks.shadowJar)
        this.group = "run velocity"
        this.doLast {
            copy {
                this.from(this@tasks.shadowJar.get().archiveFile)
                this.into(file("run/plugins"))
            }
        }
    }
    this.create<JavaExec>("runServer") {
        this.dependsOn(cloneJar)
        this.group = "run velocity"
        this.workingDir = file("run")
        this.main = "-jar"
        this.standardInput = System.`in`
        this.args = listOf(
            "velocity.jar"
        )
    }
}