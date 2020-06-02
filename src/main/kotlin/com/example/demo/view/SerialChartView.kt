package com.example.demo.view

import com.example.demo.models.InitValues
import com.example.demo.myclasses.ValidationsDash
import com.example.demo.controllers.ChartController
import com.example.demo.controllers.SerialPortController
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.chart.AreaChart
import javafx.scene.control.*
import tornadofx.*


class SerialChartView(initValues: InitValues) : Fragment() {

    val serialPortController = SerialPortController
    val validaciones = ValidationsDash()
    val chartController = ChartController
    val axisSetup = SimpleBooleanProperty(false)
    val maxXSize = SimpleBooleanProperty(false)
    lateinit var checkBoxEnableChart: CheckBox
    lateinit var checkBoxAutoRange: CheckBox
    lateinit var checkBoxContinousChart: CheckBox
    lateinit var linechartSerialChart: AreaChart<String, Number>
//    lateinit var linechartSerialChart: LineChart<String, Number>
    lateinit var texFieldMinY: TextField
    lateinit var texFieldMaxY: TextField
    lateinit var texFieldDivY: TextField
    lateinit var texFieldFixedXSize: TextField


    override val root = vbox {
        spacing = 2.0
        hbox{
            spacing = 2.0
            checkBoxEnableChart = checkbox("Habilitar gráfico", chartController.enableChartProperty) {
                action {
                    linechartSerialChart.isDisable = !isSelected
                }
            }
            texFieldMinY = textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "minY"
                disableProperty().bind(axisSetup)
                if(initValues.chartData.minY == null) {
                    text = ""
                    chartController.yAxis.lowerBound = 0.0
                }else {
                    text = initValues.chartData.minY.toString()
                    chartController.yAxis.lowerBound = initValues.chartData.minY!!
                }
                action {
                    if(texFieldMinY.text.isNotBlank() and texFieldMinY.text.isNotEmpty()) {
                        if(texFieldMinY.text.contains(validaciones.floatingString)) {
                            chartController.yAxis.lowerBound = texFieldMinY.text.toDouble()
                            initValues.chartData.minY = texFieldMinY.text.toDouble()
                        }else {
                            alertErrorInput("FLOAT miny")
                            chartController.yAxis.lowerBound = 0.0
                            initValues.chartData.minY = null
                            texFieldMinY.text = ""
                        }
                    }else {
                        chartController.yAxis.lowerBound = 0.0
                        initValues.chartData.minY = null
                        texFieldMinY.text = "0"
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.floatingString)) {
                        println("GUARDO EL DATO")
                        if(text.isBlank() or text.isEmpty()) {
                            initValues.chartData.minY = null
                            chartController.yAxis.lowerBound = 0.0
                        }else {
                            initValues.chartData.minY = text.toDouble()
                            chartController.yAxis.lowerBound = text.toDouble()
                        }
                    }else {
                        println("Sin formato decimal")
                        alertErrorInput("FLOAT miny")
//                        text = ""
                        initValues.chartData.minY = null
                        chartController.yAxis.lowerBound = 0.0
                    }
                }
            }
            texFieldMaxY = textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "maxY"
                disableProperty().bind(axisSetup)
                if(initValues.chartData.maxY == null) {
                    text = ""
                    chartController.yAxis.upperBound = 0.0
                }else {
                    text = initValues.chartData.maxY.toString()
                    chartController.yAxis.upperBound = initValues.chartData.maxY!!
                }
                action {
                    if(texFieldMaxY.text.isNotBlank() and texFieldMaxY.text.isNotEmpty()) {
                        if(texFieldMaxY.text.contains(validaciones.floatingString)) {
                            chartController.yAxis.upperBound = texFieldMaxY.text.toDouble()
                            initValues.chartData.maxY = texFieldMaxY.text.toDouble()
                        }else {
                            alertErrorInput("FLOAT maxy")
                            chartController.yAxis.upperBound = 0.0
                            initValues.chartData.maxY = null
                            texFieldMaxY.text = ""
                        }
                    }else {
                        chartController.yAxis.upperBound = 0.0
                        initValues.chartData.maxY = null
                        texFieldMaxY.text = ""
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.floatingString)) {
                        println("GUARDO EL DATO")
                        if(text.isBlank() or text.isEmpty()) {
                            initValues.chartData.maxY = null
                            chartController.yAxis.upperBound = 0.0
                        }else {
                            initValues.chartData.maxY = text.toDouble()
                            chartController.yAxis.upperBound = text.toDouble()
                        }
                    }else {
                        println("Sin formato decimal")
                        alertErrorInput("FLOAT maxy")
//                        text = ""
                        initValues.chartData.maxY = null
                        chartController.yAxis.upperBound = 0.0
                    }
                }
            }
            texFieldDivY = textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "divY"
                disableProperty().bind(axisSetup)
                if(initValues.chartData.divY == null) {
                    text = ""
                    chartController.yAxis.minorTickCount = 0
                    chartController.yAxis.tickUnit = 0.0
                }else {
                    text = initValues.chartData.divY.toString()
                    chartController.yAxis.tickUnit = initValues.chartData.divY!!.toDouble()
                    chartController.yAxis.minorTickCount = initValues.chartData.divY!!.toInt()
                }
                action {
                    if(texFieldDivY.text.isNotBlank() and texFieldDivY.text.isNotEmpty()) {
                        if(texFieldDivY.text.contains(validaciones.floatingString)) {
                            chartController.yAxis.tickUnit = texFieldDivY.text.toDouble()
                            chartController.yAxis.minorTickCount = chartController.yAxis.tickUnit.toInt()
                            initValues.chartData.divY = texFieldDivY.text.toDouble()
                        }else {
                            alertErrorInput("FLOAT divy")
                            chartController.yAxis.tickUnit = 0.0
                            chartController.yAxis.minorTickCount = 0
                            initValues.chartData.divY = null
                            texFieldDivY.text = ""
                        }
                    }else {
                        chartController.yAxis.tickUnit = 0.0
                        chartController.yAxis.minorTickCount = 0
                        initValues.chartData.divY = null
                        texFieldDivY.text = ""
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.floatingString)) {
                        println("GUARDO EL DATO")
                        if(text.isBlank() or text.isEmpty()) {
                            initValues.chartData.divY = null
                            chartController.yAxis.tickUnit = 0.0
                            chartController.yAxis.minorTickCount = 0
                        }else {
                            initValues.chartData.divY = text.toDouble()
                            chartController.yAxis.tickUnit = text.toDouble()
                            chartController.yAxis.minorTickCount = text.toInt()
                        }
                    }else {
                        println("Sin formato decimal")
                        alertErrorInput("FLOAT divy")
//                        text = ""
                        initValues.chartData.divY = null
                        chartController.yAxis.tickUnit = 0.0
                        chartController.yAxis.minorTickCount = 0
                    }
                }
            }
//            button("Aplicar") {
//                setPrefSize(90.0, 25.0)
//                setMaxSize(90.0, 25.0)
//                setMinSize(90.0, 25.0)
//                action {
//                    if(texFieldMinY.text.isNotBlank() and texFieldMinY.text.isNotEmpty()) {
//                        if(texFieldMinY.text.contains(validaciones.floatingString)) {
//                            chartController.yAxis.lowerBound = texFieldMinY.text.toDouble()
//                            initValues.chartData.minY = texFieldMinY.text.toDouble()
//                        }else {
//                            alertErrorInput("FLOAT miny")
//                            chartController.yAxis.lowerBound = 0.0
//                            initValues.chartData.minY = null
//                            texFieldMinY.text = ""
//                        }
//                    }else {
//                        chartController.yAxis.lowerBound = 0.0
//                        initValues.chartData.minY = null
//                        texFieldMinY.text = "0"
//                    }
//                    if(texFieldMaxY.text.isNotBlank() and texFieldMaxY.text.isNotEmpty()) {
//                        if(texFieldMaxY.text.contains(validaciones.floatingString)) {
//                            chartController.yAxis.upperBound = texFieldMaxY.text.toDouble()
//                            initValues.chartData.maxY = texFieldMaxY.text.toDouble()
//                        }else {
//                            alertErrorInput("FLOAT maxy")
//                            chartController.yAxis.upperBound = 0.0
//                            initValues.chartData.maxY = null
//                            texFieldMaxY.text = ""
//                        }
//                    }else {
//                        chartController.yAxis.upperBound = 0.0
//                        initValues.chartData.maxY = null
//                        texFieldMaxY.text = ""
//                    }
//                    if(texFieldDivY.text.isNotBlank() and texFieldDivY.text.isNotEmpty()) {
//                        if(texFieldDivY.text.contains(validaciones.floatingString)) {
//                            chartController.yAxis.tickUnit = texFieldDivY.text.toDouble()
//                            chartController.yAxis.minorTickCount = chartController.yAxis.tickUnit.toInt()
//                            initValues.chartData.divY = texFieldDivY.text.toDouble()
//                        }else {
//                            alertErrorInput("FLOAT divy")
//                            chartController.yAxis.tickUnit = 0.0
//                            chartController.yAxis.minorTickCount = 0
//                            initValues.chartData.divY = null
//                            texFieldDivY.text = ""
//                        }
//                    }else {
//                        chartController.yAxis.tickUnit = 10.0
//                        chartController.yAxis.minorTickCount = 10
//                        initValues.chartData.divY = null
//                        texFieldDivY.text = ""
//                    }
//                }
//            }

            checkbox("Autorango", axisSetup) {
                axisSetup.value = initValues.chartData.isAutoRangeChecked
                action {
                    initValues.chartData.isAutoRangeChecked = isSelected
                }
            }
        }
        hbox{
            spacing = 2.0
            texFieldFixedXSize = textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "Max data"
                if((initValues.chartData.dataOnChart == null) or (initValues.chartData.dataOnChart == 0)) {
                    text = ""
                    chartController.dataOnChart = 0
                }else {
                    text = initValues.chartData.dataOnChart.toString()
                    chartController.dataOnChart = initValues.chartData.dataOnChart!!
                }
                action {
                    when {
                        texFieldFixedXSize.text.isBlank() or texFieldFixedXSize.text.isEmpty() -> {
                            initValues.chartData.dataOnChart = null
                            chartController.dataOnChart = 0
                            texFieldFixedXSize.text = ""
                        }
                        texFieldFixedXSize.text == "0" -> {
                            initValues.chartData.dataOnChart = null
                            chartController.dataOnChart = 0
                            texFieldFixedXSize.text = ""
                        }
                        else -> {
                            if(texFieldFixedXSize.text.contains(validaciones.onlyNumbersString)) {
                                if(texFieldFixedXSize.text.toInt() > chartController.maxDataOnChart) {
                                    alertErrorInput("INT overflow")
                                    initValues.chartData.dataOnChart = null
                                    chartController.dataOnChart = 0
                                    text = ""
                                }else {
                                    initValues.chartData.dataOnChart = texFieldFixedXSize.text.toInt()
                                    chartController.dataOnChart = texFieldFixedXSize.text.toInt()
                                }
                            }else {
                                alertErrorInput("INT")
                                chartController.dataOnChart = 0
                                initValues.chartData.dataOnChart = 0
                                texFieldFixedXSize.text = ""
                            }
                        }
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.onlyNumbersString)) {
                        println("GUARDO EL DATO")
                        if(text.toInt() > chartController.maxDataOnChart) {
                            alertErrorInput("INT overflow")
                            initValues.chartData.dataOnChart = null
                            chartController.dataOnChart = 0
//                            text = ""
                        }else {
                            initValues.chartData.dataOnChart = texFieldFixedXSize.text.toInt()
                            chartController.dataOnChart = texFieldFixedXSize.text.toInt()
                        }
                    }else {
                        if(text != "") {
                            alertErrorInput("INT")
//                            text = ""
                        }else {
                            chartController.dataOnChart = 0
                            initValues.chartData.dataOnChart = 0
                        }
                    }
                }
            }
//            button("Aplicar") {
//                setPrefSize(90.0, 25.0)
//                setMaxSize(90.0, 25.0)
//                setMinSize(90.0, 25.0)
//                action {
//                    when {
//                        texFieldFixedXSize.text.isBlank() or texFieldFixedXSize.text.isEmpty() -> {
//                            initValues.chartData.dataOnChart = null
//                            chartController.maxDataOnChart = 0
//                            texFieldFixedXSize.text = ""
//                        }
//                        texFieldFixedXSize.text == "0" -> {
//                            initValues.chartData.dataOnChart = null
//                            chartController.maxDataOnChart = 0
//                            texFieldFixedXSize.text = ""
//                        }
//                        else -> {
//                            if(texFieldFixedXSize.text.contains(validaciones.onlyNumbersString)) {
//                                chartController.maxDataOnChart = texFieldFixedXSize.text.toInt()
//                                initValues.chartData.dataOnChart = texFieldFixedXSize.text.toInt()
//                            }else {
//                                alertErrorInput("INT")
//                                chartController.maxDataOnChart = 0
//                                initValues.chartData.dataOnChart = 0
//                                texFieldFixedXSize.text = ""
//                            }
//                        }
//                    }
//                }
//            }

            checkbox("Gráfico contínuo", maxXSize) {
                isSelected = initValues.chartData.isContinousChartChecked
                chartController.isContinuousChart.bind(maxXSize)
                action {
                    initValues.chartData.isContinousChartChecked = isSelected
                }
            }
        }
        label("Puerto seleccionado: ")
        label(serialPortController.selectedPorDescriptiveName)

        chartController.xAxis.label = "Tiempo"
//        chartController.yAxis.label = "Volume"
//        chartController.series.name = ""
//        chartController.series.node.isVisible = false

        linechartSerialChart = areachart("", chartController.xAxis, chartController.yAxis) {
            setPrefSize(950.0, 280.0)
            setMaxSize(950.0, 280.0)
            setMinSize(400.0, 200.0)
            isDisable = true
            prefHeight(300.0)
            minHeight(300.0)
            chartController.yAxis.autoRangingProperty().bind(axisSetup)
            chartController.yAxis.animated = false
            data.add(chartController.series)
            animated = false
            isLegendVisible = false
        }

        hbox {
            alignment = Pos.CENTER
            spacing = 10.0
            button("Borrar") {
                setPrefSize(90.0, 25.0)
                setMaxSize(90.0, 25.0)
                setMinSize(90.0, 25.0)
                action {
                    chartController.series.data.clear()
                    chartController.dataOnSeriesCounter = 0
                }
            }
//            button("Guardar") {
//                setPrefSize(90.0, 25.0)
//                setMaxSize(90.0, 25.0)
//                setMinSize(90.0, 25.0)
//                action {
//                }
//            }
        }
    }
    fun alertErrorInput(dataType: String, actionFn: (Alert.(ButtonType) -> Unit)? = null): Alert {
        var contentText = ""
        when(dataType) {
            "INT" -> contentText = "Formato incorrecto, no es un entero"
            "INT overflow" -> contentText = "Máxima cantidad de datos en gráfico es 400"
            "FLOAT miny" -> contentText = "Formato incorrecto en mínimo del gráfico, no es un decimal"
            "FLOAT maxy" -> contentText = "Formato incorrecto en máximo del gráfico, no es un decimal"
            "FLOAT divy" -> contentText = "Formato incorrecto en división del gráfico, no es un decimal"
        }
        val alert = Alert(Alert.AlertType.NONE, contentText, ButtonType.OK)
        alert.headerText = ""

        val buttonClicked = alert.showAndWait()
        buttonClicked.ifPresent {
            actionFn?.invoke(alert, buttonClicked.get())
            when(it.buttonData.toString()) {
                "OK_DONE" -> {
                    println("OK")
                }
            }
        }
        return alert
    }
}