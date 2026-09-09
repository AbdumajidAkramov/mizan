package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.math.BigDecimal

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val amount: BigDecimal,
    val billingCycle: String,
    val nextRenewalDate: Long,
    val icon: String,
    val color: String,
    val category: String = ""
)
