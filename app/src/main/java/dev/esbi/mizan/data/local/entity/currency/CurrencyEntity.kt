package dev.esbi.mizan.data.local.entity.currency

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.esbi.mizan.domain.model.Currency

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey
    override val code: String,    // "UZS", "USD", "EUR" - Primary Key bo'ladi
    override val name: String,    // "O'zbek so'mi", "US Dollar"
    override val symbol: String,  // "so'm", "$", "€"
    // Asosiy valyutaga nisbatan kursi (Reports va Total Balance uchun kerak)
    // Agar Code == BaseCurrency (UZS) bo'lsa, rate = 1.0 bo'ladi.
    override val rateToBase: Double = 1.0,

    override val isBaseCurrency: Boolean = false // Qaysi biri asosiy ekanligini bilish uchun
) : Currency
