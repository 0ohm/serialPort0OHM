package com.example.demo.controllers

import com.example.demo.models.AlarmsModel
import com.example.demo.models.AlarmsValue
import com.example.demo.models.AlarmsValues
import com.example.demo.controllers.LogHandler.bufferedWriter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.BufferedWriter
import java.io.FileWriter
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object AlarmsController {

    val alarmsList: ObservableList<AlarmsModel> = FXCollections.observableArrayList()
    var alarmsData = AlarmsValue()
    var alarmsAllData = AlarmsValues()
    var byteArrivedFromPortAcumulatedForAlarms = AlarmsValues()

    val isAlarmsActive = SimpleBooleanProperty()
    val higherLimit = SimpleDoubleProperty()
    val lowerLimit = SimpleDoubleProperty()
    val whichTypeOfLimits = SimpleStringProperty()

    fun addDataToAlarmList(data: AlarmsValue) {
        if (isAlarmsActive.value) {
            val date = data.date
//            alarmsData.date = date
//            alarmsData.data = data.data
            alarmsData = data
            when (whichTypeOfLimits.value) {
                "OL" -> {
                    if (data.data.toInt() > higherLimit.value) {
                        alarmsList.add(AlarmsModel(data.data, date, "", "", "", "", "", ""))
                    }
                    alarmsData.typeOfLimit = "Over Limit"
                }
                "UL" -> {
                    if (data.data.toInt() < lowerLimit.value) {
                        alarmsList.add(AlarmsModel("", "", data.data, date, "", "", "", ""))
                    }
                    alarmsData.typeOfLimit = "Under Limit"
                }
                "BL" -> {
                    if ((data.data.toInt() < higherLimit.value) and (data.data.toInt() > lowerLimit.value)) {
                        alarmsList.add(AlarmsModel("", "", "", "", data.data, date, "", ""))
                    }
                    alarmsData.typeOfLimit = "Between Limits"
                }
                "OBL" -> {
                    if ((data.data.toInt() > higherLimit.value) or (data.data.toInt() < lowerLimit.value)) {
                        alarmsList.add(AlarmsModel("", "", "", "", "", "", data.data, date))
                    }
                    alarmsData.typeOfLimit = "Outside Limits"
                }
            }
            alarmsAllData.listOfData.add(alarmsData)
        }
    }

    fun saveAlarms() {
        try {
            val nameOfAlarmLog = "Alarms ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"))}"
            val fileWriter = FileWriter("${nameOfAlarmLog}.txt", true)
            bufferedWriter = BufferedWriter(fileWriter)
            val data = GsonBuilder().setPrettyPrinting().create().toJson(alarmsAllData)
            bufferedWriter.write(data)
            bufferedWriter.flush()
            bufferedWriter.close()
            println("Datos guardados")
            println(data)
        } catch (e: Exception) {
            println("NO ENCUENTRA LA WEA DE ARCHIVO PAL LOG!... POR QUE LA BORRASTEEEEEE!!!!!!: ${e}")
        }
    }
}