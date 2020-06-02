package com.example.demo.models

import com.google.gson.annotations.SerializedName

class ChartValue(
        @SerializedName("AXISX")
        var axisX: String = "",
        @SerializedName("AXISY")
        var axisY: String = ""
)