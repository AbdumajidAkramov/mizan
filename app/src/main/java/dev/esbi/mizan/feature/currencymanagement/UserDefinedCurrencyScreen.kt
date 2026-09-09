package dev.esbi.mizan.feature.currencymanagement

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.domain.model.UnitPosition
import dev.esbi.mizan.presentation.feature.currencymanagement.CurrencyFormatter
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore
import dev.esbi.mizan.design.components.PremiumCard
import dev.esbi.mizan.design.components.PremiumCardVariant
import dev.esbi.mizan.design.components.input.MizanTextField
import dev.esbi.mizan.design.kit.icon.IconValue
import dev.esbi.mizan.design.kit.icon.MizanIcon
import dev.esbi.mizan.design.theme.colors.MizanTheme
import dev.esbi.mizan.design.utils.IconRes
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDefinedCurrencyScreen(
    store: CurrencyManagementStore,
    onBackClick: () -> Unit
) {
    val state by store.states.collectAsState(initial = CurrencyManagementStore.State())
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var exchangeRateText by remember { mutableStateOf("") }
    var unitPosition by remember { mutableStateOf(UnitPosition.FRONT) }
    var decimalDigits by remember { mutableIntStateOf(2) }

    LaunchedEffect(Unit) {
        store.labels.collect { label ->
            when (label) {
                is CurrencyManagementStore.Label.CurrencyAdded -> {
                    Toast.makeText(context, "Custom currency created!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
                is CurrencyManagementStore.Label.ShowMessage ->
                    Toast.makeText(context, label.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Custom Currency",
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        MizanIcon(
                            icon = IconValue(IconRes.ic_arrow_back),
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.background.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val rate = exchangeRateText.toBigDecimalOrNull()
                    if (rate == null) {
                        Toast.makeText(context, "Invalid exchange rate", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    store.accept(
                        CurrencyManagementStore.Intent.CreateCustomCurrency(
                            name = name,
                            unit = unit,
                            rate = rate,
                            position = unitPosition,
                            decimals = decimalDigits
                        )
                    )
                },
                containerColor = MizanTheme.premium.colors.emerald,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Check, contentDescription = "Create")
            }
        },
        containerColor = MizanTheme.premium.background.primary
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Info card
            PremiumCard(
                variant = PremiumCardVariant.Glass,
                enableInteraction = false
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "✨",
                            style = MizanTheme.typography.bodyLg,
                            color = MizanTheme.premium.colors.emerald
                        )
                    }
                    Spacer(modifier = Modifier.size(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Custom Assets",
                            style = MizanTheme.typography.bodyMd,
                            color = MizanTheme.premium.text.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Create currencies for Gold, Silver, Crypto, or any asset",
                            style = MizanTheme.typography.bodySm,
                            color = MizanTheme.premium.text.tertiary
                        )
                    }
                }
            }

            // Preview Card
            PremiumCard(
                variant = PremiumCardVariant.Glass,
                enableInteraction = false
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Preview",
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val previewAmount = BigDecimal("15000")
                    val previewRate = exchangeRateText.toBigDecimalOrNull() ?: BigDecimal.ONE
                    val previewConfig = dev.esbi.mizan.domain.model.Currency(
                        code = unit.ifBlank { "XXX" },
                        name = name.ifBlank { "Custom Currency" },
                        symbol = unit.ifBlank { "?" },
                        exchangeRate = previewRate,
                        unitPosition = unitPosition,
                        decimalDigits = decimalDigits,
                        orderIndex = 0,
                        isMainCurrency = false,
                        isUserDefined = true
                    )
                    Text(
                        text = CurrencyFormatter.format(previewAmount, previewConfig),
                        style = MizanTheme.typography.headingLg,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.animateContentSize()
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = name.ifBlank { "Enter name above" },
                        style = MizanTheme.typography.bodySm,
                        color = MizanTheme.premium.text.tertiary
                    )
                }
            }

            // Name Field
            MizanTextField(
                value = name,
                onValueChange = { name = it },
                label = "Currency Name",
                placeholder = "e.g., Gold, Kumush, USDT"
            )

            // Unit/Symbol Field
            Column {
                MizanTextField(
                    value = unit,
                    onValueChange = { unit = it.uppercase().take(5) },
                    label = "Unit / Symbol (3-5 characters)",
                    placeholder = "e.g., XAU, XAG, USDT"
                )
                Text(
                    text = "${unit.length}/5 characters",
                    style = MizanTheme.typography.bodyXs,
                    color = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }

            // Exchange Rate
            Column {
                MizanTextField(
                    value = exchangeRateText,
                    onValueChange = { exchangeRateText = it },
                    label = "Exchange Rate (to Main Currency)",
                    placeholder = "e.g., 33000000",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                state.mainCurrency?.let { mainCurrency ->
                    Text(
                        text = "1 ${unit.ifBlank { "unit" }} = ? ${mainCurrency.symbol}",
                        style = MizanTheme.typography.bodyXs,
                        color = MizanTheme.premium.text.tertiary,
                        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                    )
                }
            }

            // Unit Position Toggle
            Column {
                Text(
                    text = "Unit Position",
                    style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium),
                    color = MizanTheme.premium.text.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    listOf(
                        UnitPosition.FRONT to "Front",
                        UnitPosition.END to "End"
                    ).forEach { (pos, label) ->
                        val isSelected = unitPosition == pos
                        val exampleSymbol = unit.ifBlank { "?" }
                        val example = if (pos == UnitPosition.FRONT) "${exampleSymbol}100" else "100 $exampleSymbol"
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)
                                    else Color.White.copy(alpha = 0.05f)
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) MizanTheme.premium.colors.emerald
                                    else Color.White.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { unitPosition = pos }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = example,
                                    style = MizanTheme.typography.bodyMd,
                                    color = if (isSelected) MizanTheme.premium.colors.emerald
                                    else MizanTheme.premium.text.secondary,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = label,
                                    style = MizanTheme.typography.bodyXs,
                                    color = MizanTheme.premium.text.tertiary
                                )
                            }
                        }
                    }
                }
            }

            // Decimal Digits Dropdown
            Column {
                Text(
                    text = "Decimal Places",
                    style = MizanTheme.typography.bodyMd.copy(fontWeight = FontWeight.Medium),
                    color = MizanTheme.premium.text.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                DecimalDigitsSelector(
                    selected = decimalDigits,
                    onSelected = { decimalDigits = it }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun DecimalDigitsSelector(
    selected: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val options = (0..8).toList()
    val displayText = when (selected) {
        0 -> "None (1)"
        else -> "$selected (${"1." + "0".repeat(selected)})"
    }

    Box {
        PremiumCard(
            variant = PremiumCardVariant.Glass,
            onClick = { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = displayText,
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary
                )
                MizanIcon(
                    icon = IconValue(IconRes.ic_chevron_down),
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MizanTheme.premium.background.secondary)
        ) {
            options.forEach { digits ->
                val label = when (digits) {
                    0 -> "None (1)"
                    else -> "$digits (${"1." + "0".repeat(digits)})"
                }
                DropdownMenuItem(
                    text = {
                        Text(
                            text = label,
                            color = if (digits == selected)
                                MizanTheme.premium.colors.emerald
                            else MizanTheme.premium.text.primary,
                            fontWeight = if (digits == selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onSelected(digits)
                        expanded = false
                    }
                )
            }
        }
    }
}
