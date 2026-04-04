package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.transaction.TransactionEntity
import dev.esbi.mizan.data.local.entity.transaction.TransactionWithCurrencyEntity
import dev.esbi.mizan.domain.model.Transaction

import java.math.BigDecimal

fun TransactionWithCurrencyEntity.toDomain(): Transaction {
    return Transaction(
        id = transaction.id,
        type = Transaction.Type.valueOf(transaction.type.name),
        amount = transaction.amount,
        currency = currency.toDomain(),
        exchangeRate = transaction.exchangeRate,
        targetAmount = transaction.targetAmount,
        date = transaction.date,
        note = transaction.note,
        description = transaction.description,
        photoPaths = transaction.photoPaths,
        accountId = transaction.accountId,
        categoryId = transaction.categoryId,
        subCategoryId = null, // Removed from schema
        targetAccountId = transaction.targetAccountId,
        fee = BigDecimal.ZERO, // Removed from schema
        isBookmarked = transaction.isBookmarked,
        recurrenceRule = transaction.recurrenceRule,
        isInstallment = transaction.isInstallment,
        installmentTotalMonths = transaction.installmentTotalMonths,
        installmentCurrentMonth = transaction.installmentCurrentMonth,
        parentTransactionId = transaction.parentTransactionId,
        merchantName = transaction.merchantName,
        fiscalSign = transaction.fiscalSign
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = if (this.id == 0L) 0 else this.id,
        type = this.type,
        amount = this.amount,
        currencyCode = this.currency.code,
        exchangeRate = this.exchangeRate,
        targetAmount = this.targetAmount,
        date = this.date,
        note = this.note,
        description = this.description,
        photoPaths = this.photoPaths,
        accountId = this.accountId,
        categoryId = this.categoryId,
        targetAccountId = this.targetAccountId,
        isBookmarked = this.isBookmarked,
        recurrenceRule = this.recurrenceRule,
        isInstallment = this.isInstallment,
        installmentTotalMonths = this.installmentTotalMonths,
        installmentCurrentMonth = this.installmentCurrentMonth,
        parentTransactionId = this.parentTransactionId,
        merchantName = this.merchantName,
        fiscalSign = this.fiscalSign
    )
}
