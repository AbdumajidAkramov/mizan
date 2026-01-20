package dev.esbi.mizan.feature.addtransaction.domain.models

enum class Keypad {
    DEL,
    CLEAR,
    EQUALS,

    DIVIDE,
    MULTIPLY,
    MINUS,
    PLUS,

    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    ZERO,

    ZERO_ZERO,
    ZERO_ZERO_ZERO,
    UNKNOWN;

    companion object {
        fun key(value: String): Keypad {
            return when (value) {
                "C" -> CLEAR
                "DEL" -> DEL
                "=" -> EQUALS
                "/" -> DIVIDE
                "*" -> MULTIPLY
                "-" -> MINUS
                "+" -> PLUS
                "1" -> ONE
                "2" -> TWO
                "3" -> THREE
                "4" -> FOUR
                "5" -> FIVE
                "6" -> SIX
                "7" -> SEVEN
                "8" -> EIGHT
                "9" -> NINE
                "0" -> ZERO
                "00" -> ZERO_ZERO
                "000" -> ZERO_ZERO_ZERO
                else -> UNKNOWN
            }
        }

        fun numberChar(key: Keypad): String? {
            return when (key) {
                ONE -> "1"
                TWO -> "2"
                THREE -> "3"
                FOUR -> "4"
                FIVE -> "5"
                SIX -> "6"
                SEVEN -> "7"
                EIGHT -> "8"
                NINE -> "9"
                ZERO -> "0"
                ZERO_ZERO -> "00"
                ZERO_ZERO_ZERO -> "000"
                else -> null
            }
        }

        fun numberInt(key: Keypad): Int? {
            return when (key) {
                ONE -> 1
                TWO -> 2
                THREE -> 3
                FOUR -> 4
                FIVE -> 5
                SIX -> 6
                SEVEN -> 7
                EIGHT -> 8
                NINE -> 9
                ZERO -> 0
                ZERO_ZERO -> 100
                ZERO_ZERO_ZERO -> 1000
                else -> null
            }
        }

        fun isNumber(key: Keypad): Boolean {
            return when (key) {
                ONE,
                TWO,
                THREE,
                FOUR,
                FIVE,
                SIX,
                SEVEN,
                EIGHT,
                NINE,
                ZERO,
                ZERO_ZERO,
                ZERO_ZERO_ZERO -> true

                else -> false
            }
        }

        fun isOperator(key: Keypad): Boolean {
            return when (key) {
                DIVIDE,
                MULTIPLY,
                MINUS,
                PLUS -> true

                else -> false
            }
        }
    }
}
