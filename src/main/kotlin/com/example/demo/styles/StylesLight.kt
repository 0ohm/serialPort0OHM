package com.example.demo.styles

import javafx.geometry.Side
import javafx.scene.image.Image
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.layout.BackgroundSize
import javafx.scene.paint.Color.*
import javafx.scene.paint.Paint
import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass
import tornadofx.px
import java.net.URI
import tornadofx.*
import javax.swing.SwingConstants.CENTER

class StylesLight : Stylesheet() {
    companion object {
        val heading by cssclass()
        private val color0ohmBlueGray = c("#547884")
        private val color0ohmBlueDark = c("#405F6A")
        private val color0ohmBlueDarker = c("#243C45")
        private val color0ohmGold = c("#B28424")
    }

    init {
        setStageIcon(Image("/images/icono.jpg"))
        label and heading {
            padding = box(10.px)
            fontSize = 20.px
            fontWeight = FontWeight.BOLD
        }
        textArea {
            content {
//                backgroundColor += TRANSPARENT
//                backgroundColor += WHITE
//                backgroundRepeat += Pair(BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT)
//                backgroundPosition += BackgroundPosition(Side.LEFT, 0.5, true, Side.BOTTOM, 0.5, true)
//                backgroundImage += URI("/images/Firma.jpg")
//                backgroundSize += BackgroundSize(0.0, 0.0, true, true, true, true)
            }
//            this.bac
        }
    }
}