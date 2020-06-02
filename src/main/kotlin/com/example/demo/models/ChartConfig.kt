package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class ChartConfig (
        @SerializedName("MINY")
        var minY: Double? = null,
        @SerializedName("MAXY")
        var maxY: Double? = null,
        @SerializedName("DIVY")
        var divY: Double? = null,
        @SerializedName("AUTORANGE")
        var isAutoRangeChecked: Boolean = false,
        @SerializedName("DATAONCHART")
        var dataOnChart: Int? = null,
        @SerializedName("CONTINOUSCHART")
        var isContinousChartChecked: Boolean = false
)