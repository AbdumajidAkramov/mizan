package dev.esbi.mizan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_machine_scenarios")
data class TimeMachineScenarioEntity(
    @PrimaryKey val id: String,
    val title: String,
    val timeline: String,
    val impact: String,
    val description: String,
    val iconType: String
)
