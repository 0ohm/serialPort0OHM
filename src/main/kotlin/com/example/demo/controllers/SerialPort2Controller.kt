package com.example.demo.controllers

import com.fazecast.jSerialComm.SerialPort
import com.fazecast.jSerialComm.SerialPort.getCommPorts
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*

object SerialPort2Controller: Controller() {

    val maxLenghtOfBuffer = 500000

    val availableSerialPorts =
            FXCollections.observableArrayList<SerialPort>()
    val availableSerialPortsDescriptivePortName =
            FXCollections.observableArrayList<String>()
    val availableSerialPortsSystemPortName =
            FXCollections.observableArrayList<String>(observableList(""))
//    val selectedPortCom = SimpleStringProperty(availableSerialPortsDescriptivePortName.first())
    val selectedComPort = SimpleStringProperty()
    val baudRates = FXCollections.observableArrayList<Int>(4800, 9600, 115200, 921600)
    val selectedBaudRate = SimpleIntegerProperty(baudRates[2])

    var selectedPort: SerialPort? = null
    var selectedPorDescriptiveName = SimpleStringProperty("")

    var comPorts: Array<out SerialPort> = getCommPorts()

    var asciiArrivedFromPort = ""
    var asciiArrivedFromPortAcumulated = ""
    var asciiArrivedFromPortAcumulatedHistoric = ""

    val isAnyPortOpen = SimpleBooleanProperty(false)
    val isAnyPortOpenText = isAnyPortOpen.stringBinding {
        if (it == true) "Desconectar" else "Conectar"
    }

    init {
        for (i in comPorts) {
            availableSerialPorts += i
            availableSerialPortsDescriptivePortName += i.descriptivePortName
            availableSerialPortsSystemPortName += i.systemPortName
        }
        if(comPorts.isNotEmpty()) {
            selectedComPort.value = availableSerialPortsDescriptivePortName.first()
        }else {
            selectedComPort.value = ""
        }
    }

    fun connectToComPort(baudRate: Int): Boolean {

        selectedPort!!.baudRate = baudRate
        selectedPort!!.parity = SerialPort.NO_PARITY
        selectedPort!!.numStopBits = SerialPort.ONE_STOP_BIT
        selectedPort!!.numDataBits = 8
        selectedPort!!.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED)
        selectedPort!!.openPort()
        selectedPorDescriptiveName.value = selectedPort!!.descriptivePortName
        val buffer = ByteArray(1000)
        selectedPort!!.readBytes(buffer, 500)
        isAnyPortOpen.value = selectedPort!!.isOpen

        return selectedPort!!.isOpen
    }

    fun disconnectToComPort():Boolean {
        selectedPort!!.removeDataListener()
        selectedPort!!.closePort()
        selectedPorDescriptiveName.value = ""
        isAnyPortOpen.value = selectedPort!!.isOpen
        return selectedPort!!.isOpen
    }

    fun writeToComPort(dataToSend: String) {
        if(selectedPort != null) {
            if (selectedPort!!.isOpen) {
                selectedPort!!.writeBytes(dataToSend.toByteArray(), dataToSend.length.toLong())
            } else {
                println("Oye, abre el puerto primero!!!")
            }
        }else {
            println("No se ha creado el puerto!!!")
        }
    }

    fun writeToComPort(dataToSend: ByteArray) {
        if(selectedPort != null) {
            if (selectedPort!!.isOpen) {
                selectedPort!!.writeBytes(dataToSend, dataToSend.size.toLong())
            } else {
                println("Oye, abre el puerto primero!!!")
            }
        }else {
            println("No se ha creado el puerto!!!")
        }
    }

    var readBuffer: ByteArray? = null
    var dataSize: Int? = null
    fun getDataFromPort(f: () -> Unit) = GlobalScope.launch {
        try {
            while (true) {
                while (selectedPort!!.bytesAvailable() == 0) delay(1)
                readBuffer = ByteArray(selectedPort!!.bytesAvailable())
                dataSize = selectedPort!!.readBytes(readBuffer, readBuffer!!.size.toLong())
                println(String(readBuffer!!))
                f()
            }
        } catch (e: Exception) {
            println("ERROR EN LA RECEPCIÓN DE DATOS DEL PUERTO SERIAL: $e")
        }
    }
}