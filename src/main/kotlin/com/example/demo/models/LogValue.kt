package com.example.demo.models

import com.google.gson.annotations.SerializedName
import java.security.Timestamp
import java.time.LocalDateTime

data class LogValue (
        @SerializedName("Time")
        var dateDataArrived: String = "",
        @SerializedName("Data")
        var data: String = ""
)