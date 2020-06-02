package com.example.demo.view

import com.example.demo.models.AlarmsModel
import com.example.demo.models.AlarmsValues
import com.example.demo.models.InitValues
import com.example.demo.myclasses.ValidationsDash
import com.example.demo.controllers.AlarmsController
import javafx.collections.ListChangeListener
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ToggleGroup
import tornadofx.*


class AlarmsView(initValues: InitValues) : View("My View") {

    val alarmsController = AlarmsController

    val validaciones = ValidationsDash()
    var tableViewAlarms = tableview<AlarmsModel>()
    val toggleGroupLimits = ToggleGroup()

    override val root = vbox {
        spacing = 2.0
        hbox {
            spacing = 2.0
            checkbox("Activar alarmas", alarmsController.isAlarmsActive) {

            }
            label("Límite inferior:") {  }
            textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "Límite inferior"
                if(initValues.alarmsData.lowerLimit == null) {
                    text = ""
                    alarmsController.lowerLimit.value = 0.0
                }else {
                    text = initValues.alarmsData.lowerLimit.toString()
                    alarmsController.lowerLimit.value = text.toDouble()
                }
                action {
                    if(text.isNotBlank() and text.isNotEmpty()) {
                        if(text.contains(validaciones.floatingString)) {
                            initValues.alarmsData.lowerLimit = text.toDouble()
                            alarmsController.lowerLimit.value = text.toDouble()
                        }else {
                            alertErrorInput("FLOAT maxy")
                            initValues.alarmsData.lowerLimit = null
                            alarmsController.lowerLimit.value = 0.0
                            text = ""
                        }
                    }else {
                        initValues.alarmsData.lowerLimit = null
                        alarmsController.lowerLimit.value = 0.0
                        text = ""
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.floatingString)) {
                        println("GUARDO EL DATO")
                        if(text.isBlank() or text.isEmpty()) {
                            initValues.alarmsData.lowerLimit = null
                            alarmsController.lowerLimit.value = 0.0
                        }else {
                            initValues.alarmsData.lowerLimit = text.toDouble()
                            alarmsController.lowerLimit.value = text.toDouble()
                        }
                    }else {
                        println("Sin formato decimal")
                        alertErrorInput("LIMINF")
//                        text = ""
                        initValues.alarmsData.lowerLimit = null
                        alarmsController.lowerLimit.value = 0.0
                    }
                }
            }
            label("Límite superior:") {  }
            textfield {
                maxHeight = 25.0
                minHeight = 25.0
                prefHeight = 25.0
                maxWidth = 80.0
                promptText = "Límite superior"
                if(initValues.alarmsData.higherLimit == null) {
                    text = ""
                    alarmsController.higherLimit.value = 0.0
                }else {
                    text = initValues.alarmsData.higherLimit.toString()
                    alarmsController.higherLimit.value = text.toDouble()
                }
                action {
                    if(text.isNotBlank() and text.isNotEmpty()) {
                        if(text.contains(validaciones.floatingString)) {
                            initValues.alarmsData.higherLimit = text.toDouble()
                            alarmsController.higherLimit.value = text.toDouble()
                        }else {
                            alertErrorInput("FLOAT maxy")
                            initValues.alarmsData.higherLimit = null
                            alarmsController.higherLimit.value = 0.0
                            text = ""
                        }
                    }else {
                        initValues.alarmsData.higherLimit = null
                        alarmsController.higherLimit.value = 0.0
                        text = ""
                    }
                }
                textProperty().addListener { observableValue, pastData, newData ->
                    if(newData.contains(validaciones.floatingString)) {
                        println("GUARDO EL DATO")
                        if(text.isBlank() or text.isEmpty()) {
                            initValues.alarmsData.higherLimit = null
                            alarmsController.higherLimit.value = 0.0
                        }else {
                            initValues.alarmsData.higherLimit = text.toDouble()
                            alarmsController.higherLimit.value = text.toDouble()
                        }
                    }else {
                        println("Sin formato decimal")
                        alertErrorInput("LIMSUP")
//                        text = ""
                        initValues.alarmsData.higherLimit = null
                        alarmsController.higherLimit.value = 0.0
                    }
                }
            }
            radiobutton("Sobre límite", toggleGroupLimits) {
                isSelected = initValues.alarmsData.whichTypeOfLimits[0]
                if(isSelected) alarmsController.whichTypeOfLimits.value = "OL"
                action {
                    initValues.alarmsData.whichTypeOfLimits[0] = true
                    initValues.alarmsData.whichTypeOfLimits[1] = false
                    initValues.alarmsData.whichTypeOfLimits[2] = false
                    initValues.alarmsData.whichTypeOfLimits[3] = false
                    alarmsController.whichTypeOfLimits.value = "OL"
                }
            }
            radiobutton("Bajo límite", toggleGroupLimits) {
                isSelected = initValues.alarmsData.whichTypeOfLimits[1]
                if(isSelected) alarmsController.whichTypeOfLimits.value = "UL"
                action {
                    initValues.alarmsData.whichTypeOfLimits[0] = false
                    initValues.alarmsData.whichTypeOfLimits[1] = true
                    initValues.alarmsData.whichTypeOfLimits[2] = false
                    initValues.alarmsData.whichTypeOfLimits[3] = false
                    alarmsController.whichTypeOfLimits.value = "UL"
                }
            }
            radiobutton("Entre límites", toggleGroupLimits) {
                isSelected = initValues.alarmsData.whichTypeOfLimits[2]
                if(isSelected) alarmsController.whichTypeOfLimits.value = "BL"
                action {
                    initValues.alarmsData.whichTypeOfLimits[0] = false
                    initValues.alarmsData.whichTypeOfLimits[1] = false
                    initValues.alarmsData.whichTypeOfLimits[2] = true
                    initValues.alarmsData.whichTypeOfLimits[3] = false
                    alarmsController.whichTypeOfLimits.value = "BL"
                }
            }
            radiobutton("Fuera de límites", toggleGroupLimits) {
                isSelected = initValues.alarmsData.whichTypeOfLimits[3]
                if(isSelected) alarmsController.whichTypeOfLimits.value = "OBL"
                action {
                    initValues.alarmsData.whichTypeOfLimits[0] = false
                    initValues.alarmsData.whichTypeOfLimits[1] = false
                    initValues.alarmsData.whichTypeOfLimits[2] = false
                    initValues.alarmsData.whichTypeOfLimits[3] = true
                    alarmsController.whichTypeOfLimits.value = "OBL"
                }
            }
        }
        tableViewAlarms = tableview(alarmsController.alarmsList) {
            maxHeight = 350.0
            prefHeight = 350.0
            minHeight = 200.0
            isEditable = false
            columnResizePolicy = SmartResize.POLICY
            disableProperty().bind(!alarmsController.isAlarmsActive)
            column("Sobre límite",AlarmsModel::overLimitProperty).weightedWidth(1.0)
            column("Fecha", AlarmsModel::dateOLProperty).weightedWidth(3.0)
            column("Bajo límite", AlarmsModel::underLimitProperty).weightedWidth(1.0)
            column("Fecha",AlarmsModel::dateULProperty).weightedWidth(3.0)
            column("Entre límites", AlarmsModel::betweenLimitProperty).weightedWidth(1.0)
            column("Fecha", AlarmsModel::dateBLProperty).weightedWidth(3.0)
            column("Fuera de límites",AlarmsModel::outBetweenLimitProperty).weightedWidth(1.0)
            column("Fecha",AlarmsModel::dateOBLProperty).weightedWidth(3.0)

            items.addListener { c: ListChangeListener.Change<out AlarmsModel> ->
                c.next()
                val size = items.size
                if (size > 0) {
                    scrollTo(size - 1)
                }
            }
        }
        alignment = Pos.CENTER
        hbox {
            alignment = Pos.CENTER
            spacing = 10.0
            button("Borrar") {
                action {
                    alarmsController.alarmsList.clear()
                    alarmsController.alarmsAllData = AlarmsValues()
                }
            }
            button("Guardar") {
                disableProperty().bind(!alarmsController.isAlarmsActive)
                action {
                    alarmsController.saveAlarms()
                }
            }
        }
    }

    fun alertErrorInput(dataType: String, actionFn: (Alert.(ButtonType) -> Unit)? = null): Alert {
        var contentText = ""
        when(dataType) {
            "LIMINF" -> contentText = "Formato incorrecto en límite inferior, no es un decimal"
            "LIMSUP" -> contentText = "Formato incorrecto en límite superior, no es un decimal"
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