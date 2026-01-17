package dev.esbi.mizan.feature.financialmirror.presentation.ui.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.esbi.mizan.feature.financialmirror.domain.model.FinancialMirrorSummary
import dev.esbi.mizan.feature.financialmirror.presentation.store.FinancialMirrorStore
import dev.esbi.mizan.feature.financialmirror.presentation.ui.ANIM_DELAY_MS

@Composable
internal fun FinancialMirrorScrollContent(
    data: FinancialMirrorSummary,
    selectedView: FinancialMirrorStore.ProjectionView,
    modifier: Modifier = Modifier,
    onViewSelected: (FinancialMirrorStore.ProjectionView) -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 120.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item { AnimSection(visible, 0) { HeaderSection() } }
        item {
            AnimSection(visible, ANIM_DELAY_MS) {
                NetWorthProjectionCard(
                    data.projectionData,
                    selectedView,
                    onViewSelected
                )
            }
        }
        item { AnimSection(visible, ANIM_DELAY_MS * 2) { RiskAnalysisCard(data.riskAnalysis) } }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 3
            ) { TimeMachineSection(data.timeMachineScenarios) }
        }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 4
            ) { InvestmentOpportunitiesSection(data.investmentOpportunities) }
        }
        item {
            AnimSection(
                visible,
                ANIM_DELAY_MS * 5
            ) { AIRecommendationCard(data.aiRecommendation) }
        }
    }
}
