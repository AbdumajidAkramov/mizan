package dev.esbi.mizan.data.local.mapper

import dev.esbi.mizan.data.local.entity.transaction.TransactionWithCurrencyEntity
import dev.esbi.mizan.domain.model.Transaction

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
        subCategoryId = transaction.subCategoryId,
        targetAccountId = transaction.targetAccountId,
        fee = transaction.fee,
        isBookmarked = transaction.isBookmarked,
        recurrenceRule = transaction.recurrenceRule,
        isInstallment = transaction.isInstallment,
        installmentTotalMonths = transaction.installmentTotalMonths,
        installmentCurrentMonth = transaction.installmentCurrentMonth,
        parentTransactionId = transaction.parentTransactionId
    )
}
