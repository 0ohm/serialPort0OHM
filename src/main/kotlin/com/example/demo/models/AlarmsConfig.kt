package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class AlarmsConfig (
    @SerializedName("HIGHERLIMIT")
    var higherLimit: Double? = null,
    @SerializedName("LOWERLIMIT")
    var lowerLimit: Double? = null,
    @SerializedName("TYPEOFLIMITS")
    var whichTypeOfLimits: MutableList<Boolean> = mutableListOf(true, false, false, false)
)