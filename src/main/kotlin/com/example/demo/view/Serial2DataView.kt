package com.example.demo.view

import com.example.demo.controllers.SerialPort2Controller
import com.example.demo.myclasses.ValidationsDash
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Pos
import javafx.scene.control.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*

class Serial2DataView : Fragment() {

    val serialPortController = SerialPort2Controller

    var jobSerialPortListener: Job? = null

    lateinit var comboBoxDevices: ComboBox<String>
    lateinit var comboBoxBauds: ComboBox<Number>
    lateinit var buttonConnect: Button
    lateinit var buttonSend: Button
    lateinit var buttonClearAll: Button
    lateinit var textAreaDataArrived: TextArea
    lateinit var textAreaDataToSend: TextArea

    val toggleGroupDataTypeFromPort = ToggleGroup()
    val toggleGroupDataTypeToPort = ToggleGroup()
    var valueOfDataSelectedFromPort: String = ""
    var valueOfDataSelectedToPort: String = ""
    var deleteAsciiArrivedFromPortAcumulated = false
    val isWithEnterToPort = SimpleBooleanProperty(false)
    val validaciones = ValidationsDash()

    override val root = hbox {
        spacing = 2.0
        prefWidth = 400.0
        alignment = Pos.BASELINE_RIGHT
        vbox {
            spacing = 5.0
            hbox {
                spacing = 2.0
                comboBoxDevices = combobox(
                        serialPortController.selectedComPort,
                        serialPortController.availableSerialPortsDescriptivePortName
                ) {
                    minWidth = 180.0
                    prefWidth = 180.0
                }
                comboBoxBauds = combobox(
                        serialPortController.selectedBaudRate,
                        serialPortController.baudRates
                ) {
                    minWidth = 85.0
                    prefWidth = 85.0
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
                                        jobSerialPortListener = serialPortController.getDataFromPort {
                                            toDoWhenDataArrived()
                                        }
                                    }
                                }
                                "Desconectar" -> {
                                    val openPortState = serialPortController.disconnectToComPort()
                                    comboBoxDevices.isDisable = false
                                    comboBoxBauds.isDisable = false
                                    if(jobSerialPortListener != null) {
                                        jobSerialPortListener!!.cancel()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            textAreaDataArrived = textarea {
                setPrefSize(414.0, 265.0)
                setMinSize(60.0, 60.0)
                isEditable = false
                isWrapText = true
            }
            hbox {
                spacing = 2.0
                radiobutton("ASCII", toggleGroupDataTypeFromPort) {
                    isSelected = true
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "ASCII"
                    }
                    action {
                        textAreaDataArrived.text = serialPortController.asciiArrivedFromPortAcumulatedHistoric
                        valueOfDataSelectedFromPort = "ASCII"
                    }
                }
                radiobutton("HEX", toggleGroupDataTypeFromPort) {
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "HEX"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "HEX")
                        valueOfDataSelectedFromPort = "HEX"
                    }
                }
                radiobutton("DEC", toggleGroupDataTypeFromPort) {
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "DEC"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "DEC")
                        valueOfDataSelectedFromPort = "DEC"
                    }
                }
                radiobutton("BIN", toggleGroupDataTypeFromPort) {
                    if(isSelected) {
                        valueOfDataSelectedFromPort = "BIN"
                    }
                    action {
                        textAreaDataArrived.text = stringToSomeInteger(serialPortController.asciiArrivedFromPortAcumulatedHistoric, "BIN")
                        valueOfDataSelectedFromPort = "BIN"
                    }
                }
            }

            hbox {
                textAreaDataToSend = textarea {
                    setPrefSize(334.0, 60.0)
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
                            when (valueOfDataSelectedToPort) {
                                "ASCII" -> {
                                    if (isWithEnterToPort.value) {
                                        serialPortController.writeToComPort("${textAreaDataToSend.text}\r\n")
                                    } else {
                                        serialPortController.writeToComPort(textAreaDataToSend.text)
                                    }
                                }
                                "HEX" -> {
                                    if (textAreaDataToSend.text.contains(validaciones.hexListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if (isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        } else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    } else {
                                        alertErrorInput(valueOfDataSelectedToPort)
                                    }
                                }
                                "DEC" -> {
                                    if (textAreaDataToSend.text.contains(validaciones.integerListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if (isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        } else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    } else {
                                        alertErrorInput(valueOfDataSelectedToPort)
                                    }
                                }
                                "BIN" -> {
                                    if (textAreaDataToSend.text.contains(validaciones.binaryByteListString)) {
                                        val byteArrayToSend = validateFormatOfString(textAreaDataToSend.text, valueOfDataSelectedToPort)
                                        if (isWithEnterToPort.value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend!!)}\r\n")
                                        } else {
                                            serialPortController.writeToComPort(byteArrayToSend!!)
                                        }
                                    } else {
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
                    isSelected = true
                    if(isSelected) {
                        valueOfDataSelectedToPort = "ASCII"
                    }
                    action {
                        valueOfDataSelectedToPort = "ASCII"
                    }
                }
                radiobutton("HEX", toggleGroupDataTypeToPort) {
                    if(isSelected) {
                        valueOfDataSelectedToPort = "HEX"
                    }
                    action {
                        valueOfDataSelectedToPort = "HEX"
                    }
                }
                radiobutton("DEC", toggleGroupDataTypeToPort) {
                    if(isSelected) {
                        valueOfDataSelectedToPort = "DEC"
                    }
                    action {
                        valueOfDataSelectedToPort = "DEC"
                    }
                }
                radiobutton("BIN", toggleGroupDataTypeToPort) {
                    if(isSelected) {
                        valueOfDataSelectedToPort = "BIN"
                    }
                    action {
                        valueOfDataSelectedToPort = "BIN"
                    }
                }
                checkbox("\\n", isWithEnterToPort) {

                }
            }
        }
        actualizarUI()
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

    //Hilo para actualizar el textAreaArrived
    fun actualizarUI() {
        GlobalScope.launch {
            while(true) {
                if (deleteAsciiArrivedFromPortAcumulated == false) {
                    runLater {
                        when(valueOfDataSelectedFromPort) {
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
                        deleteAsciiArrivedFromPortAcumulated = true
                    }
                    deleteAsciiArrivedFromPortAcumulated = true
                }
                delay(10)
            }
        }
    }
}