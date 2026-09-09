package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "goals")
data class GoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val deadline: Long? = null,
    val icon: String,
    val color: String
)
