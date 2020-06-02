package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class AlarmsValues (
        @SerializedName("List of Data")
        val listOfData: MutableList<AlarmsValue> = mutableListOf()
)