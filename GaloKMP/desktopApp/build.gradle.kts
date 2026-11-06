import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(project(":shared"))

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)

    testImplementation(kotlin("test"))
    implementation("com.squareup.okio:okio:3.15.0")
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.25.2")
    implementation("org.mongodb:mongodb-driver-kotlin-sync:5.6.1")

    implementation(compose.components.resources)
}

compose.desktop {
    application {
        mainClass = "pt.isel.tds.galokmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "pt.isel.tds.galokmp"
            packageVersion = "1.0.0"
        }
    }
}