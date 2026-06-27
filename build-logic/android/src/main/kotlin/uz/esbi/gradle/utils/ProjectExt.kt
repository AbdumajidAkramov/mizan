package uz.esbi.gradle.utils

import org.gradle.api.Project
import org.gradle.api.Task

private val ANDROID_ARTIFACT_TASK_REGEX = Regex(pattern = """\b(assemble|bundle)[A-Z]\w+""")

internal val Project.currentArtifactTaskOrNull: Task?
    get() = gradle.startParameter.taskNames
        .firstNotNullOfOrNull { ANDROID_ARTIFACT_TASK_REGEX.find(it)?.value }
        ?.let(project.tasks::findByName)
