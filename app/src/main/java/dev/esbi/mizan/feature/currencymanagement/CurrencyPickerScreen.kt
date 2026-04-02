package dev.esbi.mizan.feature.currencymanagement

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.domain.model.CurrencyConfig
import dev.esbi.mizan.domain.model.UnitPosition
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.components.input.MizanTextField
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons as MizanIcons
import java.math.BigDecimal

/**
 * All available world currencies for selection.
 * Filtered at runtime to exclude Main Currency and already-added sub-currencies.
 */
private val ALL_CURRENCIES = listOf(
    Triple("USD", "US Dollar", "$"),
    Triple("EUR", "Euro", "€"),
    Triple("GBP", "British Pound", "£"),
    Triple("JPY", "Japanese Yen", "¥"),
    Triple("CNY", "Chinese Yuan", "¥"),
    Triple("RUB", "Russian Ruble", "₽"),
    Triple("KRW", "South Korean Won", "₩"),
    Triple("TRY", "Turkish Lira", "₺"),
    Triple("KZT", "Kazakh Tenge", "₸"),
    Triple("UZS", "O'zbek so'mi", "so'm"),
    Triple("AED", "UAE Dirham", "د.إ"),
    Triple("SAR", "Saudi Riyal", "﷼"),
    Triple("INR", "Indian Rupee", "₹"),
    Triple("CHF", "Swiss Franc", "CHF"),
    Triple("CAD", "Canadian Dollar", "C$"),
    Triple("AUD", "Australian Dollar", "A$"),
    Triple("BRL", "Brazilian Real", "R$"),
    Triple("MXN", "Mexican Peso", "MX$"),
    Triple("PLN", "Polish Zloty", "zł"),
    Triple("THB", "Thai Baht", "฿"),
    Triple("SGD", "Singapore Dollar", "S$"),
    Triple("HKD", "Hong Kong Dollar", "HK$"),
    Triple("SEK", "Swedish Krona", "kr"),
    Triple("NOK", "Norwegian Krone", "kr"),
    Triple("DKK", "Danish Krone", "kr"),
    Triple("CZK", "Czech Koruna", "Kč"),
    Triple("HUF", "Hungarian Forint", "Ft"),
    Triple("ILS", "Israeli Shekel", "₪"),
    Triple("ZAR", "South African Rand", "R"),
    Triple("TWD", "Taiwan Dollar", "NT$"),
    Triple("GEL", "Georgian Lari", "₾"),
    Triple("UAH", "Ukrainian Hryvnia", "₴"),
    Triple("BDT", "Bangladeshi Taka", "৳"),
    Triple("PKR", "Pakistani Rupee", "₨"),
    Triple("VND", "Vietnamese Dong", "₫")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerScreen(
    store: CurrencyManagementStore,
    onBackClick: () -> Unit,
    onAddCustomCurrencyClick: () -> Unit
) {
    val state by store.states.collectAsState(initial = CurrencyManagementStore.State())
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        store.labels.collect { label ->
            when (label) {
                is CurrencyManagementStore.Label.CurrencyAdded -> {
                    Toast.makeText(context, "Currency added!", Toast.LENGTH_SHORT).show()
                    onBackClick()
                }
                is CurrencyManagementStore.Label.ShowMessage ->
                    Toast.makeText(context, label.message, Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    // Filter out already-added currencies
    val existingCodes = state.subCurrencies.map { it.code }.toSet()
    val available = ALL_CURRENCIES
        .filter { it.first !in existingCodes }
        .filter {
            if (searchQuery.isBlank()) true
            else {
                it.first.contains(searchQuery, ignoreCase = true) ||
                        it.second.contains(searchQuery, ignoreCase = true)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Currency",
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        MizanIcon(
                            icon = IconValue(MizanIcons.ic_arrow_back),
                            tint = MizanTheme.premium.text.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MizanTheme.premium.background.primary
                )
            )
        },
        containerColor = MizanTheme.premium.background.primary
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search
            MizanTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = "Search",
                placeholder = "Search by code or name...",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                // Add Custom Currency Button
                item {
                    PremiumCard(
                        variant = PremiumCardVariant.Gradient,
                        onClick = onAddCustomCurrencyClick
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                MizanIcon(
                                    icon = IconValue(MizanIcons.ic_add),
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Add Custom Currency",
                                    style = MizanTheme.typography.bodyMd,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Create Gold, Silver, Crypto, or any asset",
                                    style = MizanTheme.typography.bodySm,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            MizanIcon(
                                icon = IconValue(MizanIcons.ic_chevron_right),
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(available, key = { it.first }) { (code, name, symbol) ->
                    PickerCurrencyRow(
                        code = code,
                        name = name,
                        symbol = symbol,
                        onClick = {
                            store.accept(
                                CurrencyManagementStore.Intent.AddCurrency(
                                    CurrencyConfig(
                                        code = code,
                                        name = name,
                                        symbol = symbol,
                                        exchangeRate = BigDecimal.ONE,
                                        unitPosition = UnitPosition.FRONT,
                                        decimalDigits = 2,
                                        orderIndex = 0,
                                        isMainCurrency = false
                                    )
                                )
                            )
                        }
                    )
                }

                if (available.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isNotBlank()) "No currencies found"
                                else "All currencies have been added",
                                color = MizanTheme.premium.text.tertiary
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun PickerCurrencyRow(
    code: String,
    name: String,
    symbol: String,
    onClick: () -> Unit
) {
    PremiumCard(
        variant = PremiumCardVariant.Glass,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Symbol badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = symbol,
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.colors.emerald,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MizanTheme.typography.bodyMd,
                    color = MizanTheme.premium.text.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Code badge inline
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = code,
                        style = MizanTheme.typography.bodyXs,
                        color = MizanTheme.premium.colors.emerald,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Add icon
            MizanIcon(
                icon = IconValue(MizanIcons.ic_add),
                tint = MizanTheme.premium.colors.emerald,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
