package com.example.demo.view

import com.example.demo.styles.Styles0ohm
import com.example.demo.styles.StylesDark
import com.example.demo.styles.StylesLight
import com.example.demo.controllers.ConfigLoader
import tornadofx.*
import tornadofx.FX.Companion.application


class MainView : View("0OHM Serial") {

    var initConfig = ConfigLoader
    var initValues = initConfig.getInicialConfig()
    val serialDataView = SerialDataView(initValues)
    val serialGraphView = SerialChartView(initValues)
    var alarmsView = AlarmsView(initValues)

    override val root = vbox {

        when(initValues.theme) {
            "0ohmTheme" -> {
                importStylesheet(Styles0ohm::class)
                removeStylesheet(StylesDark::class)
                removeStylesheet(StylesLight::class)
            }
            "DarkTheme" -> {
                removeStylesheet(Styles0ohm::class)
                importStylesheet(StylesDark::class)
                removeStylesheet(StylesLight::class)
            }
            "LightTheme" -> {
                removeStylesheet(Styles0ohm::class)
                removeStylesheet(StylesDark::class)
                importStylesheet(StylesLight::class)
            }
        }
        menubar {
            menu("Preferencias") {
                menu("Elegir tema") {
                    item("0OHM") {
                        action {
                            importStylesheet(Styles0ohm::class)
                            removeStylesheet(StylesDark::class)
                            removeStylesheet(StylesLight::class)
                            initValues.theme = "0ohmTheme"
                        }
                    }
                    item("Dark") {
                        action {
                            removeStylesheet(Styles0ohm::class)
                            importStylesheet(StylesDark::class)
                            removeStylesheet(StylesLight::class)
                            initValues.theme = "DarkTheme"
                        }
                    }
                    item("Light") {
                        action {
                            removeStylesheet(Styles0ohm::class)
                            removeStylesheet(StylesDark::class)
                            importStylesheet(StylesLight::class)
                            initValues.theme = "LightTheme"
                        }
                    }
                }
            }
            menu("Ayuda") {
                item("Manual") {
                    action {
                        application.hostServices.showDocument("https://www.0ohm.cl/0ohmserial/manual")
                    }
                }
                item("Acerca de" ) {
                    action {
                        application.hostServices.showDocument("https://www.0ohm.cl")
                    }
                }
            }
        }
        tabpane {
            tab("Comunicación Serial") {
                isClosable = false
                add(serialDataView.root)
            }
            tab("Gráfico Serial") {
                isClosable = false
                add(serialGraphView.root)
            }
            tab("Alarmas") {
                isClosable = false
                add(alarmsView.root)
            }
        }
//        background = Background(
//                BackgroundImage(
//                        Image("/images/Firma.jpg", 0.1, 0.1, true, false),
//                        BackgroundRepeat.NO_REPEAT,
//                        BackgroundRepeat.NO_REPEAT,
//                        BackgroundPosition(Side.RIGHT, 0.0, true, Side.TOP, 1.0, true),
//                        BackgroundSize(0.39, 0.20, true, true, false, false)
//                )
//        )
    }
}