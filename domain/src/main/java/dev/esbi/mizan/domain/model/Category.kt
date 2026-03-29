package dev.esbi.mizan.domain.model

interface Category {
    val id: Long

    val name: String            // "Oziq-ovqat", "Transport"
    val type: Transaction.Type  // EXPENSE yoki INCOME (Avvalgi enumdan foydalanamiz)

    // Subkategoriya mantig'i
    val parentId: Long?         // Null bo'lsa - Main Category

    // UI uchun
    val iconName: String       // Masalan: "ic_food", "emoji_apple"
    val color: String          // HEX rang kodi ("#FF5722")

    // Budjet (ixtiyoriy, kelajak uchun)
    val budgetLimit: Double?   // Shu kategoriya uchun oylik limit

    val isArchived: Boolean    // Ishlatilmaydigan kategoriyalar uchun
    val orderIndex: Int        // Ro'yxatda chiqish tartibi
}
