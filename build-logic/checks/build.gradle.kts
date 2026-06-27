plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(deps.detekt)
    implementation(files(deps.javaClass.superclass.protectionDomain.codeSource.location))
}
