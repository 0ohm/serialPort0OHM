package com.example.demo.controllers

import com.example.demo.models.InitValues
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

object ConfigLoader {

    var initValues = InitValues()

    fun getInicialConfig(): InitValues {
        try {
            val fileReader = FileReader("data.txt")
            val bufferedReader = BufferedReader(fileReader)
            initValues = Gson().fromJson(bufferedReader.readText(), InitValues::class.java)
            println("Datos cargados: \n${GsonBuilder().setPrettyPrinting().create().toJson(initValues)}")
            bufferedReader.close()
            fileReader.close()
        }catch (e: Exception) {
            initValues = InitValues()
            println("NO ENCUENTRA LA WEA DE ARCHIVO!... POR QUE LA BORRASTEEEEEE!!!!!!: ${e}")
        }
        return initValues
    }

    fun setInicialConfig() {
        try {
            val fileWriter = FileWriter("data.txt", false)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(GsonBuilder().setPrettyPrinting().create().toJson(initValues))
            bufferedWriter.flush()
            bufferedWriter.close()
            println("Datos guardados: \n${GsonBuilder().setPrettyPrinting().create().toJson(initValues)}")
        }catch (e: Exception) {
            println("NO ENCUENTRA LA WEA DE ARCHIVO!... POR QUE LA BORRASTEEEEEE!!!!!!: ${e}")
        }
    }
}