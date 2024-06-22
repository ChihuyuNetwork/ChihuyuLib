plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    `maven-publish`
}

group = "love.chihuyu"
version = "0.0.1-SNAPSHOT"
val pluginVersion: String by project.ext

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.purpurmc.org/snapshots")
}

/*
1.7.10~1.8.8: "org.github.paperspigot:paperspigot-api:$pluginVersion-R0.1-SNAPSHOT"
1.9.4~1.16.5: "com.destroystokyo.paper:paper-api:$pluginVersion-R0.1-SNAPSHOT"
1.17~1.19.4: "io.papermc.paper:paper-api:$pluginVersion-R0.1-SNAPSHOT"
 */

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:$pluginVersion-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-bukkit-shade:9.5.1")
    implementation(kotlin("stdlib"))
}

tasks {
    test {
        useJUnitPlatform()
    }

    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        from(sourceSets.main.get().resources.srcDirs) {
            filter(org.apache.tools.ant.filters.ReplaceTokens::class, mapOf("tokens" to mapOf(
                "version" to project.version.toString(),
                "name" to project.name,
                "mainPackage" to "love.chihuyu.${project.name.lowercase()}.${project.name}Plugin"
            )))
            filteringCharset = "UTF-8"
        }
    }

    shadowJar {
        exclude("org/slf4j/**")
        relocate("kotlin", "love.chihuyu.lib.kotlin")
        relocate("dev.jorel.commandapi", "love.chihuyu.lib.dev.jore.commandapi")
        relocate("org.jetbrains.annotations", "love.chihuyu.lib.org.jetbrains.annotations")
        relocate("org.intellij.lang.annotations", "love.chihuyu.lib.org.intellij.lang.annotations")
        archiveClassifier = ""
    }

    runServer {
        minecraftVersion(pluginVersion)
    }
}

publishing {
    repositories {
        maven {
            name = "repo"
            credentials(PasswordCredentials::class)
            url = uri(
                if (project.version.toString().endsWith("SNAPSHOT"))
                    "https://repo.chihuyu.love/snapshots/"
                else
                    "https://repo.chihuyu.love/releases/"
            )
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

ktlint {
    ignoreFailures.set(true)
    disabledRules.add("no-wildcard-imports")
}

kotlin {
    jvmToolchain(17)
}