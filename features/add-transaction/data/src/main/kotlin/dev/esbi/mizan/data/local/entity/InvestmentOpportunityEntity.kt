package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "investment_opportunities")
data class InvestmentOpportunityEntity(
    @PrimaryKey val id: String,
    val title: String,
    val type: String,
    val apy: String,
    val minAmount: String,
    val description: String,
    val colorHex: String
)
