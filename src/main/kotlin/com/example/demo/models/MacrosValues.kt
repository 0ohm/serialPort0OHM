package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class MacrosValues (
        @SerializedName("MACRO")
        var previousMacro: String = "",
        @SerializedName("HEX")
        var isHexChecked: Boolean = false,
        @SerializedName("ENTER")
        var isEnterChecked: Boolean = false,
        @SerializedName("DELAY")
        var delay: String = "",
        @SerializedName("SELECTED")
        var isSelectedChecked: Boolean = false
)