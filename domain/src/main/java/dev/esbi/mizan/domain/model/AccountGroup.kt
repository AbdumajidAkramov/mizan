package dev.esbi.mizan.domain.model

abstract class AccountGroup {
    abstract val id: Long
    abstract val name: String        // "Accounts", "Savings", "Dreams"
    abstract val iconName: String?   // Guruh uchun umumiy ikonka
    abstract val orderIndex: Int?    // Ro'yxatdagi tartibi
    abstract val type: AccountGroupType
    abstract val isSystemGroup: Boolean // System groups cannot be deleted

    companion object {
        operator fun invoke(
            id: Long = 0,
            name: String = "",
            type: AccountGroupType = AccountGroupType.DEFAULT,
            iconName: String? = null,
            orderIndex: Int? = null,
            isSystemGroup: Boolean = false,
        ): AccountGroup {
            return DefaultImpl(
                id = id,
                name = name,
                iconName = iconName,
                orderIndex = orderIndex,
                type = type,
                isSystemGroup = isSystemGroup,
            )
        }

        fun AccountGroup.copy(
            id: Long = this.id,
            name: String = this.name,
            iconName: String? = this.iconName,
            orderIndex: Int? = this.orderIndex,
            type: AccountGroupType = this.type,
            isSystemGroup: Boolean = this.isSystemGroup,
        ): AccountGroup {
            return invoke(
                id = id,
                name = name,
                iconName = iconName,
                orderIndex = orderIndex,
                type = type,
                isSystemGroup = isSystemGroup,
            )
        }

        private data class DefaultImpl(
            override val id: Long,
            override val name: String,
            override val iconName: String?,
            override val orderIndex: Int?,
            override val type: AccountGroupType,
            override val isSystemGroup: Boolean,
        ) : AccountGroup()
    }
}
