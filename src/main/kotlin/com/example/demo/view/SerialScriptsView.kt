package com.example.demo.view

import com.example.demo.models.InitValues
import com.example.demo.myclasses.ValidationsDash
import com.example.demo.controllers.SerialPortController
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import tornadofx.*

class SerialScriptsView(initValues: InitValues) : View("My View") {

    val serialPortController = SerialPortController
    val validaciones = ValidationsDash()

    val checkBoxSelectedMacroListProperty = SimpleListProperty("","", observableList(SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false)))
    val checkBoxSelectedHEXListProperty = SimpleListProperty("","", observableList(SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false)))
    val checkBoxSelectedEnterListProperty = SimpleListProperty("","", observableList(SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false),SimpleBooleanProperty(false)))
    val macroToSendListProperty = SimpleListProperty("", "", observableList(SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty()))
    val delayToSendListProperty = SimpleListProperty("", "", observableList(SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty(), SimpleStringProperty()))
    override val root = vbox {
        spacing = 2.0
        maxWidth = 414.0
        prefWidth = 414.0
        hbox {
            spacing = 8.0
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    checkbox("$i", checkBoxSelectedMacroListProperty[i]) {
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        isSelected = initValues.macros[i].isSelectedChecked
                        action {
                            initValues.macros[i].isSelectedChecked = isSelected
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    textfield {
                        promptText = "Ingresa tu Macro"
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        maxWidth = 120.0
                        macroToSendListProperty[i].value = if(initValues.macros[i].previousMacro.isBlank() or initValues.macros[i].previousMacro.isEmpty()) {
                            ""
                        }else {
                            initValues.macros[i].previousMacro
                        }
                        action {
                            if(text.isBlank() or text.isEmpty()) {
                                initValues.macros[i].previousMacro = ""
                            }else {
                                initValues.macros[i].previousMacro = text
                            }
                            if(checkBoxSelectedHEXListProperty[i].value) {
                                checkFormatOfString(text, "HEX")
                            }
                        }
                        textProperty().bindBidirectional(macroToSendListProperty[i])
                        textProperty().addListener { observableValue, pastData, newData ->
                            initValues.macros[i].previousMacro = text
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    checkbox("HEX", checkBoxSelectedHEXListProperty[i]) {
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        isSelected = initValues.macros[i].isHexChecked
                        action {
                            initValues.macros[i].isHexChecked = isSelected
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    checkbox("\\n", checkBoxSelectedEnterListProperty[i]) {
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        isSelected = initValues.macros[i].isEnterChecked
                        action {
                            initValues.macros[i].isEnterChecked = isSelected
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    button("Enviar") {
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        disableProperty().bind(!(serialPortController.isAnyPortOpen and checkBoxSelectedMacroListProperty[i]))
                        action {
                            println("Envio: ${macroToSendListProperty[i].value} desde $i, con formato HEX: ${checkBoxSelectedHEXListProperty[i].value}, con enter: ${checkBoxSelectedEnterListProperty[i].value}")
                            if(!checkBoxSelectedHEXListProperty[i].value) {
                                if(checkBoxSelectedEnterListProperty[i].value) {
                                    serialPortController.writeToComPort("${macroToSendListProperty[i].value}\r\n")
                                }else {
                                    serialPortController.writeToComPort(macroToSendListProperty[i].value)
                                }
                            }else {
                                val byteArrayToSend = checkFormatOfString(macroToSendListProperty[i].value, "HEX")
                                if(byteArrayToSend != null) {
                                    if(checkBoxSelectedEnterListProperty[i].value) {
                                        serialPortController.writeToComPort("${String(byteArrayToSend)}\r\n")
                                    }else {
                                        serialPortController.writeToComPort(byteArrayToSend)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    textfield {
                        promptText = "Delay"
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                        prefWidth = 50.0
                        delayToSendListProperty[i].value = if(initValues.macros[i].delay.isBlank() or initValues.macros[i].delay.isEmpty()) {
                            ""
                        }else {
                            initValues.macros[i].delay
                        }
                        text = delayToSendListProperty[i].value
                        action {
                            when {
                                text.isBlank() or text.isEmpty() -> {
                                    initValues.macros[i].delay = ""
                                    text = ""
                                }
                                text.toInt() == 0 -> {
                                    initValues.macros[i].delay = "0"
                                    text = ""
                                }
                                else -> {
                                    initValues.macros[i].delay = text
                                }
                            }
                        }
                        textProperty().addListener { observableValue, pastData, newData ->
                            if(newData.contains(validaciones.onlyNumbersString)) {
                                println("GUARDO EL DATO")
                                if(text.isBlank() or text.isEmpty()) {
                                    initValues.macros[i].delay = ""
                                    delayToSendListProperty[i].value = "0"
                                }else {
                                    initValues.macros[i].delay = text
                                    delayToSendListProperty[i].value = text
                                }
                            }else {
                                println("Sin formato decimal")
                                alertErrorInput("DELAY")
                                initValues.macros[i].delay = ""
                                delayToSendListProperty[i].value = "0"
                            }
                        }
                    }
                }
            }
            vbox {
                spacing = 2.0
                for (i in 0..9) {
                    label("[ms]") {
                        maxHeight = 25.0
                        minHeight = 25.0
                        prefHeight = 25.0
                    }
                }
            }
        }
        button("Ejecutar") {
            action {
                if(serialPortController.isAnyPortOpen.value) {
                    GlobalScope.launch {
                        for (i in 0..9) {
                            if (checkBoxSelectedMacroListProperty[i].value) {
                                println("$i está checkeado")
                                if (!checkBoxSelectedHEXListProperty[i].value) {
                                    if (checkBoxSelectedEnterListProperty[i].value) {
                                        serialPortController.writeToComPort("${macroToSendListProperty[i].value}\r\n")
                                    } else {
                                        serialPortController.writeToComPort(macroToSendListProperty[i].value)
                                    }
                                } else {
                                    val byteArrayToSend = checkFormatOfString(macroToSendListProperty[i].value, "HEX")
                                    if (byteArrayToSend != null) {
                                        if (checkBoxSelectedEnterListProperty[i].value) {
                                            serialPortController.writeToComPort("${String(byteArrayToSend)}\r\n")
                                        } else {
                                            serialPortController.writeToComPort(byteArrayToSend)
                                        }
                                    }
                                }
                            }
                            if (delayToSendListProperty[i].value.isNotEmpty() and delayToSendListProperty[i].value.isNotBlank()) {
                                delay(delayToSendListProperty[i].value.toLong())
                            }
                        }
                        cancel()
                    }
                }else {
                    println("Ningún puerto conectado")
                }
            }
        }
    }

    fun checkFormatOfString(str: String, format: String): ByteArray? {
        if(str.isEmpty()) {
            println("Sin texto para enviar al puerto COM")
            return null
        }
        val strList = str.split(" ")
        val strByteArray: ByteArray = ByteArray(strList.size)
        when(format) {
            "HEX" -> {
                val regexp = ValidationsDash().hexListString
                if(!str.contains(regexp)) {
                    alertErrorInput("HEX")
                    return null
                }
                for ((j, i) in strList.withIndex()) {
                    strByteArray[j] = Integer.parseInt(i.subSequence(2, 4).toString(), 16).toByte()
                }
            }
        }
        return strByteArray
    }

    fun alertErrorInput(dataType: String, actionFn: (Alert.(ButtonType) -> Unit)? = null): Alert {
        var contentText = ""
        when(dataType) {
            "HEX" -> contentText = "Formato de HEX: 0x00 hasta 0x7F, separados por espacio"
            "DELAY" -> contentText = "Formato de DELAY: 123456789... sólo números"
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
