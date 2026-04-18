package dev.esbi.mizan.feature.currencymanagement

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import dev.esbi.mizan.domain.model.Currency
import dev.esbi.mizan.domain.model.UnitPosition
import dev.esbi.mizan.presentation.feature.currencymanagement.CurrencyFormatter
import dev.esbi.mizan.presentation.feature.currencymanagement.store.CurrencyManagementStore
import dev.esbi.mizan.ui.components.PremiumCard
import dev.esbi.mizan.ui.components.PremiumCardVariant
import dev.esbi.mizan.ui.kit.icon.IconValue
import dev.esbi.mizan.ui.kit.icon.MizanIcon
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import dev.esbi.mizan.ui.utils.Icons as MizanIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubCurrencyListScreen(
    store: CurrencyManagementStore,
    onBackClick: () -> Unit,
    onAddCurrencyClick: () -> Unit,
    onCurrencySettingsClick: (String) -> Unit
) {
    val state by store.states.collectAsState(initial = CurrencyManagementStore.State())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        store.labels.collect { label ->
            when (label) {
                is CurrencyManagementStore.Label.ShowMessage ->
                    Toast.makeText(context, label.message, Toast.LENGTH_SHORT).show()
                is CurrencyManagementStore.Label.CurrencyRemoved ->
                    Toast.makeText(context, "Currency removed", Toast.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Sub-Currencies",
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCurrencyClick,
                containerColor = MizanTheme.premium.colors.emerald,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Currency")
            }
        },
        containerColor = MizanTheme.premium.background.primary
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading && state.subCurrencies.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MizanTheme.premium.colors.emerald
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(4.dp)) }

                    // Main currency header
                    state.mainCurrency?.let { main ->
                        item {
                            Text(
                                text = "Main Currency",
                                style = MizanTheme.typography.bodyMd,
                                color = MizanTheme.premium.text.secondary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        item {
                            MainCurrencyCard(config = main)
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                    }

                    // Sub currencies header
                    val subs = state.subCurrencies.filter { !it.isMainCurrency }
                    if (subs.isNotEmpty()) {
                        item {
                            Text(
                                text = "Sub-Currencies",
                                style = MizanTheme.typography.bodyMd,
                                color = MizanTheme.premium.text.secondary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        itemsIndexed(
                            items = subs,
                            key = { _, item -> item.code }
                        ) { _, config ->
                            SubCurrencyRow(
                                config = config,
                                mainCurrency = state.mainCurrency,
                                onClick = { onCurrencySettingsClick(config.code) },
                                onDelete = {
                                    store.accept(
                                        CurrencyManagementStore.Intent.RemoveCurrency(config.code)
                                    )
                                }
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // Syncing overlay
            AnimatedVisibility(
                visible = state.isSyncing,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = MizanTheme.premium.colors.emerald)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Syncing rates...",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MainCurrencyCard(config: Currency) {
    PremiumCard(
        variant = PremiumCardVariant.Gradient,
        enableInteraction = false
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Currency badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.symbol,
                    style = MizanTheme.typography.headingLg,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = config.name,
                    style = MizanTheme.typography.bodyLg,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = config.code,
                    style = MizanTheme.typography.bodySm,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "MAIN",
                    style = MizanTheme.typography.bodyXs,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SubCurrencyRow(
    config: Currency,
    mainCurrency: Currency?,
    onClick: () -> Unit,
    onDelete: () -> Unit
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
            // Currency symbol badge
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = config.symbol,
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.colors.emerald,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = config.name,
                        style = MizanTheme.typography.bodyMd,
                        color = MizanTheme.premium.text.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Code badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MizanTheme.premium.colors.emerald.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = config.code,
                            style = MizanTheme.typography.bodyXs,
                            color = MizanTheme.premium.colors.emerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Exchange rate info
                val rateText = if (mainCurrency != null) {
                    val formatted = CurrencyFormatter.format(
                        config.exchangeRate,
                        mainCurrency
                    )
                    "1 ${config.code} = $formatted"
                } else {
                    "Rate: ${config.exchangeRate.toPlainString()}"
                }
                Text(
                    text = rateText,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.tertiary
                )

                // Position & decimals info
                val posText = if (config.unitPosition == UnitPosition.FRONT) "Front" else "End"
                Text(
                    text = "Position: $posText · Decimals: ${config.decimalDigits}",
                    style = MizanTheme.typography.bodyXs,
                    color = MizanTheme.premium.text.tertiary
                )
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MizanTheme.premium.text.tertiary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
