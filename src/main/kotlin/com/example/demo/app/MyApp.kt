package com.example.demo.app

import com.example.demo.styles.Styles0ohm
import com.example.demo.view.MainView
import com.example.demo.controllers.ConfigLoader
import com.example.demo.controllers.SerialPortController
import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MainView::class, Styles0ohm::class) {

    val configLoader = ConfigLoader
    val serialPortController = SerialPortController

    override fun start(stage: Stage) {
        with(stage) {
            width = 1000.0
            height = 500.0
        }
        super.start(stage)
    }

    override fun stop() {
        if(serialPortController.isAnyPortOpen.value) {
            println("Desconectando puerto serial")
            serialPortController.disconnectToComPort()
        }
        configLoader.setInicialConfig()
        super.stop()
    }
}