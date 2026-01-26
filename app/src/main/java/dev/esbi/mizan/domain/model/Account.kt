package dev.esbi.mizan.domain.model


interface Account {
    val id: Long
    val name: String
    val type: Type
    val initialBalance: Double
    val currencyCode: String

    val iconName: String?       // Ikonka nomi (resurs ID emas, string. Masalan: "ic_card")
    val color: String?          // HEX rang kodi (Masalan: "#4FACFE")

    val isArchived: Boolean // O'chirib yubormasdan, arxivlash uchun
    val excludeFromTotal: Boolean // Umumiy balansda ko'rsatmaslik uchun

    enum class Type {
        CASH,       // Naqd pul
        CARD,       // Plastik karta (Uzcard, Humo, Visa)
        SAVINGS,    // Omonat / Yig'im
        DEBT,       // Qarz (Men birovdan qarzdorman yoki birov mendan)
        INVESTMENT  // Investitsiya
    }
}