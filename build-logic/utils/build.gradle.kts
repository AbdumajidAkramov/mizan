plugins {
    `kotlin-dsl`
}

dependencies {
    // workaround https://github.com/gradle/gradle/issues/15383
    api(files(deps.javaClass.superclass.protectionDomain.codeSource.location))
}
