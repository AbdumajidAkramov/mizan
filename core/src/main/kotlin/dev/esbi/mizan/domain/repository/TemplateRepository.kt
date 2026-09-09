package dev.esbi.mizan.domain.repository

import dev.esbi.mizan.domain.model.Template
import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    fun getAllTemplates(): Flow<List<Template>>
    suspend fun addTemplate(template: Template): Long
}