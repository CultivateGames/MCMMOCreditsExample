group = "games.cultivate"
version = "0.4.4"
description = "MCMMOCreditsExample"

plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("org.cadixdev.licenser") version "0.6.1"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    //MCMMO
    maven("https://nexus.neetgames.com/repository/maven-releases/")
}

dependencies {
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")
    //MCMMO Credits
    compileOnly("games.cultivate:MCMMOCredits:0.4.4")
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")

    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.1.222") {
        exclude("com.sk89q.worldguard")
        exclude("com.sk89q.worldedit")
    }
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

license {
    style.put("java", "DOUBLE_SLASH")
    newLine(false)
    exclude("**/*.yml")
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
        //MCMMO spams legacy text and run-task adds this flag by default.
        systemProperty("net.kyori.adventure.text.warnWhenLegacyFormattingDetected", false)
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