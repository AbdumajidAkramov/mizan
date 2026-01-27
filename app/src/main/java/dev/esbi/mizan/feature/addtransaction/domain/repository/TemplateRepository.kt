package dev.esbi.mizan.feature.addtransaction.domain.repository

import dev.esbi.mizan.domain.model.Template
import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    fun getAllTemplates(): Flow<List<Template>>
}
