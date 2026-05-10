package dev.esbi.mizan.feature.calc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.esbi.mizan.feature.calc.mvikotlin.CalculatorStore.Intent
import dev.esbi.mizan.ui.kit.text.MizanResizableAmount
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.math.BigDecimal

@Composable
fun MizanCalculatorScreen(viewModel: MizanCalculatorViewModel) {
    // Store'dan state'ni kuzatamiz
    val state by viewModel.calculatorState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxWidth()
        ) {
            // Displey qismi
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(16.dp)
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    text = state.expression,
                    fontSize = 24.sp,
                    color = MizanTheme.premium.text.primary
                )

                MizanResizableAmount(
                    amount = state.currentValue.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    maxFontSize = 56.sp
                )
            }

            // NumberPad (Tugmalar)
            val buttons = listOf(
                listOf("C", "÷", "×", "<"),
                listOf("7", "8", "9", "-"),
                listOf("4", "5", "6", "+"),
                listOf("1", "2", "3", "="),
                listOf("0", "00", "000", ".")
            )

            buttons.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    row.forEach { label ->
                        CalcButton(
                            label = label,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val intent = when (label) {
                                    "C" -> Intent.Clear
                                    "<" -> Intent.Delete
                                    "=" -> Intent.Evaluate
                                    else -> Intent.Input(label)
                                }
                                viewModel.onCalculatorIntent(intent)
                            }
                        )
                    }
                }
            }
        }

    }
}