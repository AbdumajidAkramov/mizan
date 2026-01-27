package dev.esbi.mizan.feature.addtransaction.data.repository

import dev.esbi.mizan.data.local.dao.TemplateDao
import dev.esbi.mizan.domain.model.Template
import dev.esbi.mizan.feature.addtransaction.domain.repository.TemplateRepository
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
                    categoryId = entity.categoryId
                )
            }
        }
    }
}
