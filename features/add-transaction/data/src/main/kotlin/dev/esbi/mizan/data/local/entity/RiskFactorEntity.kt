package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "risk_factors")
data class RiskFactorEntity(
    @PrimaryKey val category: String,
    val score: Int,
    val status: String,
    val description: String
)
