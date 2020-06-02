package com.example.demo.models

import javafx.beans.property.SimpleStringProperty
import tornadofx.getValue
import tornadofx.setValue

class AlarmsModel(overLimit: String, dateOL: String, underLimit: String, dateUL: String, betweenLimit: String, dateBL: String, outBetweenLimit: String, dateOBL: String) {
    val dateOBLProperty = SimpleStringProperty(dateOBL)
    var dateOBL by dateOBLProperty
    val outBetweenLimitProperty = SimpleStringProperty(outBetweenLimit)
    var outBetweenLimit by outBetweenLimitProperty
    val dateBLProperty = SimpleStringProperty(dateBL)
    var dateBL by dateBLProperty
    val betweenLimitProperty = SimpleStringProperty(betweenLimit)
    var betweenLimit by betweenLimitProperty
    val dateULProperty = SimpleStringProperty(dateUL)
    var dateUL by dateULProperty
    val underLimitProperty = SimpleStringProperty(underLimit)
    var underLimit by underLimitProperty
    val dateOLProperty = SimpleStringProperty(dateOL)
    var dateOL by dateOLProperty
    val overLimitProperty = SimpleStringProperty(overLimit)
    var overLimit by overLimitProperty
}
