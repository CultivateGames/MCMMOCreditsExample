group = "games.cultivate"
version = "0.0.1"
description = "MCMMOCreditsExample"

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.5.0"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    //MCMMO Credits
    compileOnly("games.cultivate:MCMMOCredits:0.3.3")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
        options.compilerArgs.add("-parameters")
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    runServer {
        minecraftVersion("1.19.3")
    }

    shadowJar {
        minimize {
            exclude(dependency("com.github.ben-manes.caffeine:caffeine:3.0.3"))
        }
        relocate("io.leangen", "games.cultivate.relocate.io.leangen")
        relocate("org.spongepowered", "games.cultivate.relocate.org.spongepowered")

        manifest {
            attributes(Pair("Main-Class", "games.cultivate.creditsexample.MCMMOCreditsExample"))
        }
    }
}