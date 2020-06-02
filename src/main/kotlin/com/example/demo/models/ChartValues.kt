package com.example.demo.models

import com.google.gson.annotations.SerializedName

class ChartValues(
        @SerializedName("List of Data")
        val listOfData: MutableList<ChartValue> = mutableListOf()
)