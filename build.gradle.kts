group = "games.cultivate"
version = "0.4.1"
description = "MCMMOCreditsExample"

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    //MCMMO Credits
    compileOnly("games.cultivate:MCMMOCredits:0.4.1")

    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.20-R0.1-SNAPSHOT")
}

bukkit {
    name = project.name
    version = project.version.toString()
    main = "games.cultivate.creditsexample.MCMMOCreditsExample"
    apiVersion = "1.19"
    description = "MCMMO Credits plugin example."
    authors = listOf("CultivateGames")
    website = "https://cultivate.games/"
    depend = listOf("MCMMOCredits")
}

tasks {
    assemble {
        dependsOn(shadowJar)
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
        minecraftVersion("1.20.1")
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