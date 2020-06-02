package com.example.demo.models

import com.google.gson.annotations.SerializedName

data class AlarmsValues (
        @SerializedName("LISTOFDATA")
        val listOfData: MutableList<AlarmsValue> = mutableListOf()
)