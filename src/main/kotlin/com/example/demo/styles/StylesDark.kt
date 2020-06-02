package com.example.demo.styles

import javafx.scene.image.Image
import javafx.scene.paint.Color.*
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px
import tornadofx.*

class StylesDark : Stylesheet() {
    companion object {
        val heading by cssclass()
        private val colorGray = c("#636064")
        private val colorDark = c("#3e3e42")
        private val colorDarker = c("#2d2e33")
        private val colorMetal = c("#615d5e")
        private val colorMetalMate = c("#797475")
        private val colorWhiteDark = c("#cbcbcb")
    }

    init {
        setStageIcon(Image("/images/icono.jpg"))
        menuBar {
            baseColor = colorDark
        }
        root {
            backgroundColor += colorDark
        }
        tab {
            baseColor= colorDarker
            fontWeight = FontWeight.BOLD
            textFill = colorMetalMate
        }
        tabHeaderBackground {
            backgroundColor += colorDark
            fontWeight = FontWeight.BOLD
            textFill = colorMetalMate
        }
        label {
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        textArea {
            content {
                backgroundColor += colorGray
            }
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        comboBox {
            baseColor = colorMetal
            backgroundColor += colorGray
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
            label {
                textFill = colorWhiteDark
            }
        }
        textField {
            backgroundColor += colorGray
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        button {
            baseColor = colorMetal
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        checkBox {
            baseColor = colorMetalMate
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        radioButton {
            baseColor = colorMetalMate
            fontWeight = FontWeight.BOLD
            textFill = colorWhiteDark
        }
        chart {
            baseColor = colorGray
            areaLegendSymbol {
                textFill = WHITE
            }
            axis {
                baseColor = colorWhiteDark
                label{
                    textFill = WHITE
                    fontWeight = FontWeight.BOLD
                }
                tickLabelFill = WHITE
            }
        }
        tableView {
            baseColor = colorGray
            fontWeight = FontWeight.BOLD
            cell {
                textFill = colorDarker
            }
        }
    }
}