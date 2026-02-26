package dev.esbi.mizan.feature.addtransaction.domain.model

internal const val DEL_KEY = "DEL"
internal const val CLEAR_KEY = "C"
internal const val EQUAL_KEY = "="

enum class Keypad {
    DELETE,
    CLEAR,
    EQUAL,

    DIVIDE,
    MULTIPLY,
    MINUS,
    PLUS,

    DOT,
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
        val operators: List<Keypad> = listOf(DIVIDE, MULTIPLY, MINUS, PLUS)
        val numbers: List<Keypad> = listOf(
            ZERO,
            ONE,
            TWO,
            THREE,
            FOUR,
            FIVE,
            SIX,
            SEVEN,
            EIGHT,
            NINE,
            ZERO_ZERO,
            ZERO_ZERO_ZERO
        )

        fun key(value: String): Keypad {
            return when (value) {
                CLEAR_KEY -> CLEAR
                DEL_KEY -> DELETE
                EQUAL_KEY -> EQUAL
                "÷" -> DIVIDE
                "×" -> MULTIPLY
                "-" -> MINUS
                "+" -> PLUS
                "." -> DOT
                "0" -> ZERO
                "1" -> ONE
                "2" -> TWO
                "3" -> THREE
                "4" -> FOUR
                "5" -> FIVE
                "6" -> SIX
                "7" -> SEVEN
                "8" -> EIGHT
                "9" -> NINE
                "00" -> ZERO_ZERO
                "000" -> ZERO_ZERO_ZERO
                else -> UNKNOWN
            }
        }

        fun number(key: Keypad): String {
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
                else -> ""
            }
        }

        fun isNumber(key: Keypad): Boolean = key in numbers

        fun isOperator(key: Keypad): Boolean = key in operators
        fun operator(key: Keypad): String {
            return when (key) {
                DIVIDE -> "÷"
                MULTIPLY -> "×"
                MINUS -> "-"
                PLUS -> "+"
                else -> ""
            }
        }
    }
}
