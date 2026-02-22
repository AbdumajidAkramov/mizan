package dev.esbi.mizan.domain.model

import java.math.BigDecimal

data class Amount(
    val value: BigDecimal = BigDecimal("0.0"),
    val currency: String = "UZS"
)
