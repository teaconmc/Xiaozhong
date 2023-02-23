import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import net.minecraftforge.gradle.userdev.UserDevExtension
import org.gradle.jvm.tasks.Jar

buildscript {
    repositories {
        maven { url = uri("https://maven.minecraftforge.net") }
        mavenCentral()
    }
    dependencies {
        classpath(group = "net.minecraftforge.gradle", name = "ForgeGradle", version = "5.1.+")
    }
}
plugins {
    `kotlin-dsl`
    id("eclipse")
    id("maven-publish")
}
apply(plugin = "net.minecraftforge.gradle")

version = "1.0"
group = "org.teacon"
base.archivesName.set("Xiaozhong")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println("Java: ${System.getProperty("java.version")}, JVM: ${System.getProperty("java.vm.version")} (${System.getProperty("java.vendor")}), Arch: ${System.getProperty("os.arch")}")

configure<UserDevExtension> {
    mappings("official", "1.18.2")
    runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create("xiaozhong") {
                    source(sourceSets.getByName("main"))
                }
            }
        }
        create("server") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            mods {
                create("xiaozhong") {
                    source(sourceSets.getByName("main"))
                }
            }
        }
        create("gameTestServer") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", "xiaozhong")
            mods {
                create("xiaozhong") {
                    source(sourceSets.getByName("main"))
                }
            }
        }
        create("data") {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            args("--mod", "xiaozhong",
                "--all", "--output", file("src/generated/resources/"), "--existing", file("src/main/resources/")
            )
            mods {
                create("xiaozhong") {
                    source(sourceSets.getByName("main"))
                }
            }
        }
    }
}

sourceSets.getByName("main").resources { srcDir("src/generated/resources") }

repositories {
    /****************/
}
dependencies {
    "minecraft"("net.minecraftforge:forge:1.18.2-40.1.19")
}

val jar: Jar = tasks.getByName<Jar>("jar")
jar.apply {
    manifest {
        attributes(mapOf(
            "Specification-Title" to "Xiaozhong",
            "Specification-Vendor" to "TeaConMC",
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to jar.archiveVersion,
            "Implementation-Vendor" to "TeaConMC",
            "Implementation-Timestamp" to OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        ))
    }
}

jar.finalizedBy("reobfJar")

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifact(jar)
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/mcmodsrepo")
        }
    }
}

