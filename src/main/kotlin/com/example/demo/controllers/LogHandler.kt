package com.example.demo.controllers

import com.example.demo.models.LogValues
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import java.io.BufferedWriter
import java.io.FileWriter
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object LogHandler {

    val logValuesInteger = LogValues()
    val nameOfLog = "LOG ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"))}"

    var corrutinaGuardadoPeriodico: Job? = null
    lateinit var fileWriter: FileWriter
    lateinit var bufferedWriter: BufferedWriter

    fun openLog() {
        fileWriter = FileWriter("${nameOfLog}.txt", true)
        bufferedWriter = BufferedWriter(fileWriter)
    }

    fun startToSaveLog(data: LogValues) {
        try {
//            val fileWriter = FileWriter("${nameOfLog}.txt", true)
//            bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(GsonBuilder().setPrettyPrinting().create().toJson(data))
            bufferedWriter.flush()
//            bufferedWriter.close()
            logValuesInteger.listOfData.clear()
            println("Datos guardados")// \n${Gson().toJson(data)}")
        } catch (e: Exception) {
            println("NO ENCUENTRA LA WEA DE ARCHIVO PAL LOG!... POR QUE LA BORRASTEEEEEE!!!!!!: ${e}")
        }
    }

    fun stopSavingLog() {
        corrutinaGuardadoPeriodico!!.cancel()
        bufferedWriter.close()
    }
}