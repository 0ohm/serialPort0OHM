package com.example.demo.controllers

import com.example.demo.models.ChartValue
import com.example.demo.models.ChartValues
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import java.time.LocalDateTime

object ChartController {

    val enableChartProperty = SimpleBooleanProperty(false)
    var byteArrivedFromPortAcumulatedForChart = ChartValues()

    val xAxis = CategoryAxis()
    val yAxis = NumberAxis(0.0, 5.0, 1.0)

    //defining a series
    val series = XYChart.Series<String, Number>()

    var dataOnSeriesCounter = 0
    var dataOnChart = 100
    var maxDataOnChart = 400
    var isContinuousChart = SimpleBooleanProperty(false)

    fun addDataToSerie(newData: Int) {
        val dataToSerie: XYChart.Data<String, Number> = XYChart.Data(LocalDateTime.now().toString(), newData)
        series.data.add(dataToSerie)
        series.node.setOnMouseClicked {
            println("X: ${dataToSerie.xValue}, Y: ${dataToSerie.yValue}")
        }
        dataOnSeriesCounter++
        if(dataOnSeriesCounter > dataOnChart) {
            dataOnSeriesCounter = 0
            series.data.clear()
        }
    }

    fun addDataToSerie(data: ChartValue) {
        val dataForSerie = XYChart.Data(data.axisX, data.axisY.toInt() as Number)
        if(!isContinuousChart.value) {
            if(dataOnSeriesCounter >= dataOnChart) {
                dataOnSeriesCounter = 0
                series.data.clear()
            }
            series.data.add(dataForSerie)
            dataOnSeriesCounter++
        }else {
            series.data.add(dataForSerie)
            dataOnSeriesCounter++
            if(dataOnSeriesCounter > dataOnChart) {
                dataOnSeriesCounter = dataOnSeriesCounter--
                series.data.remove(0, 1)
            }
        }
    }
}