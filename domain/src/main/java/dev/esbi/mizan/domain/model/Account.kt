package dev.esbi.mizan.domain.model

data class Account(
    val id: Long,
    val groupId: Long,
    val name: String,
    val balance: Double,
    val currency: Currency,
    val isArchived: Boolean,
    val excludeFromTotal: Boolean,
    val description: String? = null,
    val isDeleted: Boolean = false
)
