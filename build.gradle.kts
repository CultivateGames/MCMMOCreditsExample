group = "games.cultivate"
version = "0.3.7"
description = "MCMMOCreditsExample"

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.0.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    //MCMMO Credits
    compileOnly("games.cultivate:MCMMOCredits:0.3.7")
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
        minecraftVersion("1.19.4")
    }

    shadowJar {
        minimize()
        relocate("io.leangen", "games.cultivate.relocate.io.leangen")
        relocate("org.spongepowered", "games.cultivate.relocate.org.spongepowered")

        manifest {
            attributes(Pair("Main-Class", "games.cultivate.creditsexample.MCMMOCreditsExample"))
        }
    }
}