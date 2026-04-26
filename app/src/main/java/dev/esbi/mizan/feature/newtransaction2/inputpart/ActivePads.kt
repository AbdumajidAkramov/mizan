package dev.esbi.mizan.feature.newtransaction2.inputpart

sealed interface ActivePads {
    class DatePad : ActivePads
    class AmountPad : ActivePads
    class AccountPad : ActivePads
    class CategoryPad : ActivePads
    class Keyboard : ActivePads
}
