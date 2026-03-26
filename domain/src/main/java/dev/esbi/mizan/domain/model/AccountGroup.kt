package dev.esbi.mizan.domain.model

interface AccountGroup {
    val id: Long
    val name: String        // "Accounts", "Savings", "Dreams"
    val iconName: String?   // Guruh uchun umumiy ikonka
    val orderIndex: Int?  // Ro'yxatdagi tartibi
}
