package dev.esbi.mizan.feature.newtransaction.inputpart

sealed interface ActivePads {
    class DatePad : ActivePads
    class AmountPad : ActivePads
    class AccountPad : ActivePads
    class CategoryPad : ActivePads
    class Keyboard : ActivePads
}
