package dev.esbi.mizan.data.local.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    val code: String,    // "UZS", "USD", "EUR" - Primary Key bo'ladi

    val name: String,    // "O'zbek so'mi", "US Dollar"
    val symbol: String,  // "so'm", "$", "€"

    // Asosiy valyutaga nisbatan kursi (Reports va Total Balance uchun kerak)
    // Agar Code == BaseCurrency (UZS) bo'lsa, rate = 1.0 bo'ladi.
    val rateToBase: Double = 1.0,

    val isBaseCurrency: Boolean = false // Qaysi biri asosiy ekanligini bilish uchun
)
