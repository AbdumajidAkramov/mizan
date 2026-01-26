package dev.esbi.mizan.domain.model


interface Account {
    val id: Long
    val groupId: Long // <--- MAJBURIY BOG'LANISH
    val name: String
    val type: Type
    val balance: Double
    val currencyCode: String

    val iconName: String?
        get() = null    // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    val color: String?
        get() = null  // HEX rang kodi (Masalan: "#4FACFE")

    val isArchived: Boolean // O'chirib yubormasdan, arxivlash uchun
    val excludeFromTotal: Boolean // Umumiy balansda ko'rsatmaslik uchun

    val description: String?
        get() = null

    enum class Type {
        CASH,       // Naqd pul
        CARD,       // Plastik karta (Uzcard, Humo, Visa)
        SAVINGS,    // Omonat / Yig'im
        DEBT,       // Qarz (Men birovdan qarzdorman yoki birov mendan)
        INVESTMENT  // Investitsiya
    }
}