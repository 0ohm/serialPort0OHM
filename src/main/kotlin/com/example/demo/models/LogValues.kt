package com.example.demo.models

import com.google.gson.annotations.SerializedName

class LogValues (
        @SerializedName("List of Data")
        val listOfData: MutableList<LogValue> = mutableListOf()
)