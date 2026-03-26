package dev.esbi.mizan.data.local.entity.category

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.esbi.mizan.domain.model.CategoryMetadata

@Entity(tableName = "category_metadata")
data class CategoryMetadataEntity(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    override val label: String,
    override val groupType: String, // EXPENSE / INCOME
    override val slug: String,      // "expense_must", "income_active"
    override val color: String?
) : CategoryMetadata