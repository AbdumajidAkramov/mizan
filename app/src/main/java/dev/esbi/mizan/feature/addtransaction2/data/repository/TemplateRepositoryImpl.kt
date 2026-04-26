package dev.esbi.mizan.feature.addtransaction2.data.repository

import dev.esbi.mizan.data.local.dao.TemplateDao
import dev.esbi.mizan.data.local.entity.template.TemplateEntity
import dev.esbi.mizan.domain.model.Template
import dev.esbi.mizan.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TemplateRepositoryImpl @Inject constructor(
    private val templateDao: TemplateDao
) : TemplateRepository {

    override fun getAllTemplates(): Flow<List<Template>> {
        return templateDao.getAllTemplates().map { entities ->
            entities.map { entity ->
                Template(
                    id = entity.id,
                    name = entity.name,
                    amount = entity.amount,
                    iconName = entity.iconName,
                    transactionType = entity.transactionType,
                    categoryId = entity.categoryId,
                    accountId = entity.accountId,
                    note = entity.note
                )
            }
        }
    }

    override suspend fun addTemplate(template: Template): Long {
        val entity = TemplateEntity(
            id = template.id,
            name = template.name,
            amount = template.amount,
            iconName = template.iconName,
            transactionType = template.transactionType,
            categoryId = template.categoryId,
            accountId = template.accountId,
            note = template.note
        )
        return templateDao.insert(entity)
    }
}
