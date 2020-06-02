package com.example.demo.styles

import javafx.scene.image.Image
import javafx.scene.paint.Color.*
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px
import tornadofx.*

class Styles0ohm : Stylesheet() {
    companion object {
        val heading by cssclass()
        private val color0ohmBlueGray = c("#547884")
        private val color0ohmBlueDark = c("#405F6A")
        private val color0ohmBlueDarker = c("#243C45")
        private val color0ohmGold = c("#B28424")
        private val color0ohmGoldMate = c("#b29457")
        private val color0ohmGoldWhite = c("#ffdf9b")
    }

    init {
        setStageIcon(Image("/images/icono.jpg"))
        menuBar {
            textFill = RED
            baseColor = color0ohmBlueDark
        }
        root {
            backgroundColor += color0ohmBlueDark
        }
        tab {
            baseColor= color0ohmBlueDarker
            fontWeight = FontWeight.BOLD
            textFill = color0ohmGoldMate
        }
        tabHeaderBackground {
            backgroundColor += color0ohmBlueDark
            fontWeight = FontWeight.BOLD
            textFill = color0ohmGoldMate
        }
        label {
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        textArea {
            content {
                backgroundColor += color0ohmBlueGray
            }
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        comboBox {
            baseColor = color0ohmGold
            backgroundColor += color0ohmBlueGray
            fontWeight = FontWeight.BOLD
            textFill = WHITE
            label {
                textFill = WHITE
            }
        }
        textField {
            backgroundColor += color0ohmBlueGray
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        button {
            baseColor = color0ohmGold
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        checkBox {
            baseColor = color0ohmGoldMate
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        radioButton {
            baseColor = color0ohmGoldMate
            fontWeight = FontWeight.BOLD
            textFill = WHITE
        }
        chart {
            baseColor = color0ohmGoldWhite
            areaLegendSymbol {
                textFill = WHITE
            }
            axis {
                baseColor = color0ohmBlueDarker
                label{
                    textFill = WHITE
                    fontWeight = FontWeight.BOLD
                }
                tickLabelFill = WHITE
            }
        }
        tableView {
            baseColor = color0ohmGoldMate
            fontWeight = FontWeight.BOLD
            cell {
                textFill = color0ohmBlueDarker
            }
        }
    }
}