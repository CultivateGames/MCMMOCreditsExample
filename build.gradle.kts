group = "games.cultivate"
version = "0.0.1"
description = "MCMMOCreditsExample"

plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.papermc.paperweight.userdev") version "1.3.6"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

    //MCMMO Credits
    //maven("https://jitpack.io")
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    implementation("cloud.commandframework:cloud-annotations:1.8.0")
    implementation("cloud.commandframework:cloud-paper:1.8.0")
    implementation("org.spongepowered:configurate-yaml:4.2.0-SNAPSHOT")

    //MCMMO Credits
    //compileOnly("com.github.CultivateGames:MCMMOCredits:v0.3.0")
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

    shadowJar {
        minimize {
            exclude(dependency("com.github.ben-manes.caffeine:caffeine:3.0.3"))
        }
        relocate("cloud.commandframework", "games.cultivate.relocate.cloud.commandframework")
        relocate("org.incendo.interfaces", "games.cultivate.relocate.org.incendo.interfaces")
        relocate("io.leangen", "games.cultivate.relocate.io.leangen")
        relocate("org.spongepowered", "games.cultivate.relocate.org.spongepowered")

        manifest {
            attributes(Pair("Main-Class", "games.cultivate.MCMMOCreditsExample"))
        }
    }
}