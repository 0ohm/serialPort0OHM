package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class AlarmsValue (
    @SerializedName("TYPEOFLIMIT")
    var typeOfLimit: String = "",
    @SerializedName("DATA")
    var data: String = "",
    @SerializedName("DATE")
    var date: String = ""
)