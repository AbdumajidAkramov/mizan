package dev.esbi.mizan.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import dev.esbi.mizan.data.local.entity.template.TemplateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {

    @Query("SELECT * FROM templates ORDER BY name ASC")
    fun getAllTemplates(): Flow<List<TemplateEntity>>
}
