package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class InitValues (
        @SerializedName("MACROS")
        var macros: List<MacrosValues> = listOf(MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues(), MacrosValues()),
        @SerializedName("DATATYPEFROMPORT")
        var dataTypeFromPort: MutableList<Boolean> = mutableListOf(true, false, false, false),
        @SerializedName("DATATYPETOPORT")
        var dataTypeToPort: MutableList<Boolean> = mutableListOf(true, false, false, false),
        @SerializedName("CHART")
        var chartData: ChartConfig = ChartConfig(),
        @SerializedName("ALARMS")
        var alarmsData: AlarmsConfig = AlarmsConfig(),
        @SerializedName("THEME")
        var theme: String = "0ohmTheme"
)