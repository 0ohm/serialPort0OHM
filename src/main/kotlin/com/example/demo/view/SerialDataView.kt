package com.example.demo.view

import com.example.demo.models.AlarmsValue
import com.example.demo.models.ChartValue
import com.example.demo.models.InitValues
import com.example.demo.models.LogValue
import com.example.demo.myclasses.ValidationsDash
import com.example.demo.controllers.AlarmsController
import com.example.demo.controllers.ChartController
import com.example.demo.controllers.LogHandler
import com.example.demo.controllers.SerialPortController
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.*
import javafx.scene.layout.VBox
import kotlinx.coroutines.*
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SerialDataView(initValues: InitValues) : Fragment() {

    val serialScriptsView = SerialScriptsView(initValues)
    val serial2DataView = Serial2DataView()
    val logHandler = LogHandler

    val serialPortController = SerialPortController
    val chartController = ChartController
    val alarmsController = AlarmsController

    var jobSerialPortListener: Job? = null
    var startUiJob: Job? = null
    val validaciones = ValidationsDash()

    lateinit var comboBoxDevices: ComboBox<String>
    lateinit var comboBoxBauds: ComboBox<Number>
    lateinit var buttonConnect: Button
    lateinit var buttonSend: Button
    lateinit var buttonClearAll: Button
    lateinit var textAreaDataArrived: TextArea
    lateinit var textAreaDataToSend: TextArea
    lateinit var vBoxRightSideNode: VBox
    lateinit var checkBoxLog: CheckBox

    val toggleGroupDataTypeFromPort = ToggleGroup()
    val toggleGroupDataTypeToPort = ToggleGroup()

    val isScriptNodeSelected = SimpleBooleanProperty(false)
    val onCheckScriptSelect = isScriptNodeSelected.objectBinding() {
        if (it == true) serialScriptsView.root else serial2DataView.root
    }
    val onCheckSecondPortSelect = isScriptNodeSelected.objectBinding() {
        if (it == true) serial2DataView.root else serialScriptsView.root
    }

    var valueOfDataSelectedFromPort: String = ""
    var valueOfDataSelectedToPort: String = ""
    val isWithEnterToPort = SimpleBooleanProperty(false)
    var deleteAsciiArrivedFromPortAcumulated = false

    override val root = hbox {
        spacing = 2.0
        vbox {
            prefWidth = 584.0
            spacing = 5.0
            hbox {
                spacing = 2.0
                label("Puertos disponibles:")
                comboBoxDevices = combobox(
                        serialPortController.selectedComPort,
                        serialPortController.availableSerialPortsDescriptivePortName
                ) {
                    minWidth = 185.0
                    prefWidth = 185.0
                }
                label("Baudios:")
                comboBoxBauds = combobox(
                        serialPortController.selectedBaudRate,
                        serialPortController.baudRates
                ) {
                    minWidth = 90.0
                    prefWidth = 90.0
                }
                buttonConnect = button(serialPortController.isAnyPortOpenText) {
                    setPrefSize(90.0, 25.0)
                    setMaxSize(90.0, 25.0)
                    setMinSize(90.0, 25.0)
                    action {
                        if(comboBoxDevices.selectionModel.selectedIndex >= 0) {
                            serialPortController.selectedPort = serialPortController.availableSerialPorts[comboBoxDevices.selectionModel.selectedIndex]
                            when (buttonConnect.text) {
                                "Conectar" -> {
                                    val openPortState =
                                            serialPortController.connectToComPort(
                                                    comboBoxBauds.selectedItem!!.toInt()
                                            )
                                    if (openPortState) {
                                        comboBoxDevices.isDisable = true
                                        comboBoxBauds.isDisable = true
                                        startUiJob = actualizarUI()
                                        jobSerialPortListener = serialPortController.getDataFromPort {
                                            toDoWhenDataArrived()
                                        }
                                    }
                                }
                                "Desconectar" -> {
                                    val openPortState = serialPortController.disconnectToComPort()
                                    comboBoxDevices.isDisable = false
                                    comboBoxBauds.isDisable = false
                                    startUiJob!!.cancel()
                                    if(jobSerialPortListener != null) {
                                        jobSerialPortListener!!.cancel()
                                    }
                                    if(logHandler.corrutinaGuardadoPeriodico != null) {
                                        logHandler.stopSavingLog()
                                    }
                                    checkBoxLog.isSelected = false
                                }
                            }
                        }
                    }
                }
            }

            textAreaDataArrived = textarea {
                setPrefSize(584.0, 265.0)
                setMinSize(60.0, 60.0)
                isEditable = false
                isWrapText = true
            }

            hbox {
                spacing = 2.0
                radiobutton("ASCII", toggleGroupDataTypeFromPort) {
                    isSelected = initValues.dataTypeFromPort[0]
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "ASCII"
                    }
                    action {
                        textAreaDataArrived.text = serialPortController.asciiArrivedFromPortAcumulatedHistoric
                        initValues.dataTypeFromPort[0] = true
                        initValues.dataTypeFromPort[1] = false
                        initValues.dataTypeFromPort[2] = false
                        initValues.dataTypeFromPort[3] = false
                        valueOfDataSelectedFromPort = "ASCII"
                    }
                }
                radiobutton("HEX", toggleGroupDataTypeFromPort) {
                    isSelected = initValues.dataTypeFromPort[1]
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "HEX"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "HEX")
                        initValues.dataTypeFromPort[0] = false
                        initValues.dataTypeFromPort[1] = true
                        initValues.dataTypeFromPort[2] = false
                        initValues.dataTypeFromPort[3] = false
                        valueOfDataSelectedFromPort = "HEX"
                    }
                }
                radiobutton("DEC", toggleGroupDataTypeFromPort) {
                    isSelected = initValues.dataTypeFromPort[2]
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "DEC"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "DEC")
                        initValues.dataTypeFromPort[0] = false
                        initValues.dataTypeFromPort[1] = false
                        initValues.dataTypeFromPort[2] = true
                        initValues.dataTypeFromPort[3] = false
                        valueOfDataSelectedFromPort = "DEC"
                    }
                }
                radiobutton("BIN", toggleGroupDataTypeFromPort) {
                    isSelected = initValues.dataTypeFromPort[3]
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "BIN"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "BIN")
                        initValues.dataTypeFromPort[0] = false
                        initValues.dataTypeFromPort[1] = false
                        initValues.dataTypeFromPort[2] = false
                        initValues.dataTypeFromPort[3] = true
                        valueOfDataSelectedFromPort = "BIN"
                    }
                }
            }

            hbox {
                textAreaDataToSend = textarea {
                    setPrefSize(504.0, 60.0)
                    setMinSize(60.0, 60.0)
                }
                vbox {
                    buttonClearAll = button("Borrar") {
                        setPrefSize(80.0, 30.0)
                        setMaxSize(80.0, 30.0)
                        setMinSize(80.0, 30.0)
                        action {
                            textAreaDataArrived.clear()
                            serialPortController.asciiArrivedFromPortAcumulatedHistoric = ""
                        }
                    }
                    buttonSend = button("Enviar") {
                        setPrefSize(80.0, 30.0)
                        setMaxSize(80.0, 30.0)
                        setMinSize(80.0, 30.0)
                        action {
                            when(valueOfDataSelectedToPort) {
                                "ASCII" -> {
                                    if(isWithEnterToPort.value) {
                                        serialPortController.writeToComPort("${textAreaDataToSend.text}\r\n")
                                    }else {
                                        serialPortController.writeToComPort(textAreaDataToSend.text)
                                    }
                                }
                                "HEX" -> {
                                    if(textAreaDataToSend.text.contains(validaciones.hexListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if(isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        }else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    }else {
                                        alertErrorInput(valueOfDataSelectedToPort)
                                    }
                                }
                                "DEC" -> {
                                    if(textAreaDataToSend.text.contains(validaciones.integerListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if(isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        }else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    }else {
                                        alertErrorInput(valueOfDataSelectedToPort)
                                    }
                                }
                                "BIN" -> {
                                    if(textAreaDataToSend.text.contains(validaciones.binaryByteListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if(isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        }else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    }else {
                                        alertErrorInput(valueOfDataSelectedToPort)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            hbox {
                spacing = 2.0
                radiobutton("ASCII", toggleGroupDataTypeToPort) {
                    isSelected = initValues.dataTypeToPort[0]
                    if(isSelected) {
                        valueOfDataSelectedToPort = "ASCII"
                    }
                    action {
                        initValues.dataTypeToPort[0] = true
                        initValues.dataTypeToPort[1] = false
                        initValues.dataTypeToPort[2] = false
                        initValues.dataTypeToPort[3] = false
                        valueOfDataSelectedToPort = "ASCII"
                    }
                }
                radiobutton("HEX", toggleGroupDataTypeToPort) {
                    isSelected = initValues.dataTypeToPort[1]
                    if(isSelected) {
                        valueOfDataSelectedToPort = "HEX"
                    }
                    action {
                        initValues.dataTypeToPort[0] = false
                        initValues.dataTypeToPort[1] = true
                        initValues.dataTypeToPort[2] = false
                        initValues.dataTypeToPort[3] = false
                        valueOfDataSelectedToPort = "HEX"
                    }
                }
                radiobutton("DEC", toggleGroupDataTypeToPort) {
                    isSelected = initValues.dataTypeToPort[2]
                    if(isSelected) {
                        valueOfDataSelectedToPort = "DEC"
                    }
                    action {
                        initValues.dataTypeToPort[0] = false
                        initValues.dataTypeToPort[1] = false
                        initValues.dataTypeToPort[2] = true
                        initValues.dataTypeToPort[3] = false
                        valueOfDataSelectedToPort = "DEC"
                    }
                }
                radiobutton("BIN", toggleGroupDataTypeToPort) {
                    isSelected = initValues.dataTypeToPort[3]
                    if(isSelected) {
                        valueOfDataSelectedToPort = "BIN"
                    }
                    action {
                        initValues.dataTypeToPort[0] = false
                        initValues.dataTypeToPort[1] = false
                        initValues.dataTypeToPort[2] = false
                        initValues.dataTypeToPort[3] = true
                        valueOfDataSelectedToPort = "BIN"
                    }
                }
                checkbox("\\n", isWithEnterToPort) {

                }
                checkbox("Scripts/Dual", isScriptNodeSelected) {
                    action {
                        vBoxRightSideNode.children!!.remove(onCheckScriptSelect.value)
                        vBoxRightSideNode.children!!.add(onCheckSecondPortSelect.value)
//                        println((toggleGroupDataType.selectedToggle as RadioButton).textProperty().value)
                    }
                }
                checkBoxLog = checkbox("Log") {
                    action {
                        if (serialPortController.isAnyPortOpen.value) {
                            if (isSelected) {
                                logHandler.openLog()
                                logHandler.corrutinaGuardadoPeriodico = GlobalScope.launch(Dispatchers.IO) {
                                    while (true) {
                                        logHandler.startToSaveLog(logHandler.logValuesInteger)
                                        delay(5000)
                                    }
                                }
                            } else {
                                logHandler.stopSavingLog()
                            }
                        }else {
                            isSelected = false
                        }
                    }
                }
            }
        }
        vBoxRightSideNode = vbox {
            prefWidth = 414.0
            add(serialScriptsView.root)
        }
    }

    fun toDoWhenDataArrived() {
        if(deleteAsciiArrivedFromPortAcumulated == true) {
            serialPortController.asciiArrivedFromPortAcumulated = ""
            deleteAsciiArrivedFromPortAcumulated = false
        }
        if(serialPortController.asciiArrivedFromPortAcumulatedHistoric.length >= serialPortController.maxLenghtOfBuffer) {
            serialPortController.asciiArrivedFromPortAcumulatedHistoric = ""
            textAreaDataArrived.text = ""
        }
        serialPortController.asciiArrivedFromPort = String(serialPortController.readBuffer!!)
        serialPortController.asciiArrivedFromPortAcumulated += serialPortController.asciiArrivedFromPort
        serialPortController.asciiArrivedFromPortAcumulatedHistoric += serialPortController.asciiArrivedFromPort

        if(alarmsController.isAlarmsActive.value) {
            for (i in serialPortController.readBuffer!!) {
                alarmsController.byteArrivedFromPortAcumulatedForAlarms.listOfData.add(AlarmsValue("nada", i.toInt().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS"))))
            }
        }
        if(chartController.enableChartProperty.value) {
            for (i in serialPortController.readBuffer!!) {
                chartController.byteArrivedFromPortAcumulatedForChart.listOfData.add(ChartValue(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS")), i.toInt().toString()))
            }
        }

        if (checkBoxLog.isSelected) {
            when (valueOfDataSelectedFromPort) {
                "ASCII" -> {
                    logHandler.logValuesInteger.listOfData.add(LogValue(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS")),
                            serialPortController.asciiArrivedFromPort
                    )
                    )
                }
                "HEX" -> {
                    logHandler.logValuesInteger.listOfData.add(LogValue(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS")),
                            stringToSomeInteger(serialPortController.asciiArrivedFromPort, "HEX")
                    )
                    )
                }
                "DEC" -> {
                    runLater {
                        logHandler.logValuesInteger.listOfData.add(LogValue(
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS")),
                                stringToSomeInteger(serialPortController.asciiArrivedFromPort, "DEC")
                        )
                        )
                    }
                }
                "BIN" -> {
                    logHandler.logValuesInteger.listOfData.add(LogValue(
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SS")),
                            stringToSomeInteger(serialPortController.asciiArrivedFromPort, "BIN")
                    )
                    )
                }
            }
        }
    }

    fun validateFormatOfString(str: String, format: String): ByteArray? {
        if(str.isEmpty()) {
            println("Sin texto para enviar al puerto COM")
            return null
        }

        val strList = str.split(" ")
        val strByteArray: ByteArray = ByteArray(strList.size)
        when(format) {
            "DEC" -> {
                for ((j, i) in strList.withIndex()) {
                    val regexp = ValidationsDash().integerString
                    if(!i.contains(regexp)) {
                        alertErrorInput(format)
                        return null
                    } else {
                        strByteArray[j] = i.toInt().toByte()
                    }
                }
            }
            "HEX" -> {
                val regexp = ValidationsDash().hexListString
                if(!str.contains(regexp)) {
                    alertErrorInput(format)
                    return null
                }
                for ((j, i) in strList.withIndex()) {
                    strByteArray[j] = Integer.parseInt(i.subSequence(2, 4).toString(), 16).toByte()
                }
            }
            "BIN" -> {
                val regexp = ValidationsDash().binaryByteListString
                if(!str.contains(regexp)) {
                    alertErrorInput(format)
                    return null
                }
                for ((j, i) in strList.withIndex()) {
                     strByteArray[j] = Integer.parseInt(i.subSequence(2, 10).toString(),2).toByte()
                }
            }
        }
        return strByteArray
    }

    fun alertErrorInput(dataType: String, actionFn: (Alert.(ButtonType) -> Unit)? = null): Alert {
        var contentText = ""
        when(dataType) {
            "ASCII" -> contentText = "Formato de ASCII: abcd!? ..."
            "HEX" -> contentText = "Formato de HEX: 0x00 hasta 0x7F, separados por espacio"
            "DEC" -> contentText = "Formato de DEC: 0 hasta 127, separados por espacio"
            "BIN" -> contentText = "Formato de BIN: 0b00000000 hasta 0b01111111, separados por espacio"
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

    fun stringToSomeInteger(data: String, type: String): String {
        val newData = data.toByteArray()
        var dataToReturn = ""
        for (i in newData) {
            when(type) {
                "HEX" -> {
                    dataToReturn += String.format("0x%02X ", i)
                }
                "DEC" -> {
                    dataToReturn += String.format("%d ", i)
                }
                "BIN" -> {
                    dataToReturn += String.format("0b%8s_", Integer.toBinaryString(i.toInt() and 0xFF)).replace(" ", "0").replace("_", " ")
                }
            }
        }
        return dataToReturn
    }

    //Hilo para actualizar el textAreaArrived
    fun actualizarUI() = GlobalScope.launch {
        while(true) {
            runLater {
                if (deleteAsciiArrivedFromPortAcumulated == false) {
                    when (valueOfDataSelectedFromPort) {
                        "ASCII" -> {
                            textAreaDataArrived.text += serialPortController.asciiArrivedFromPortAcumulated
                        }
                        "HEX" -> {
                            textAreaDataArrived.text += stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulated, "HEX")
                        }
                        "DEC" -> {
                            textAreaDataArrived.text += stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulated, "DEC")
                        }
                        "BIN" -> {
                            textAreaDataArrived.text += stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulated, "BIN")
                        }
                    }
                    textAreaDataArrived.end()

                    if (chartController.enableChartProperty.value) {
                        val listOfActualbyteArrivedFromPortAcumulatedForChart = chartController.byteArrivedFromPortAcumulatedForChart.listOfData.toList()
                        chartController.byteArrivedFromPortAcumulatedForChart.listOfData.clear()
                        for (data in listOfActualbyteArrivedFromPortAcumulatedForChart) {
                            chartController.addDataToSerie(data)
                        }
                    }

                    if (alarmsController.isAlarmsActive.value) {
                        val listOfActualbyteArrivedFromPortAcumulatedForAlarms = alarmsController.byteArrivedFromPortAcumulatedForAlarms.listOfData.toList()
                        alarmsController.byteArrivedFromPortAcumulatedForAlarms.listOfData.clear()
                        for (data in listOfActualbyteArrivedFromPortAcumulatedForAlarms) {
                            alarmsController.addDataToAlarmList(data)
                        }
                    }
                }
                deleteAsciiArrivedFromPortAcumulated = true
            }
            delay(10)
        }
    }
}