package dev.esbi.mizan.domain.model

data class Account(
    val id: Long,
    val groupId: Long, // <--- MAJBURIY BOG'LANISH
    val name: String,
    val type: Type,
    val balance: Double,
    val currency: Currency,
    val iconName: String? = null,    // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    val color: String? = null,       // HEX rang kodi (Masalan: "#4FACFE")
    val isArchived: Boolean,
    val excludeFromTotal: Boolean,
    val description: String? = null,
    val isDeleted: Boolean = false
) {
    enum class Type {
        CASH,       // Naqd pul
        BANK,       // Bank account (previously CARD)
        SAVINGS,    // Omonat / Yig'im
        CREDIT,     // Qarz (previously DEBT)
        INVESTMENT,  // Investitsiya
        CARD,
        DEBT
    }
}
