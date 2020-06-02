package com.example.demo.myclasses

class ValidationsDash {

    val integerString = "^[0-1][0-2][0-7]\$|^[0-9][0-9]\$|^[0-9]\$".toRegex()
    val integerListString = "^(^[0-1][0-2][0-7]|^[0-9][0-9]|^[0-9])+(?: ([0-1][0-2][0-7]|[0-9][0-9]|[0-9]))*\$".toRegex()
    val hexString = "^([0](?i)(x)\\d{2}|[0](?i)(x)(\\d[a-fA-F]))\$".toRegex()
    val hexListString = "^(^([0](?i)(x)\\d{2}|[0](?i)(x)(\\d[a-fA-F])))+(?: (([0](?i)(x)\\d{2}|[0](?i)(x)(\\d[a-fA-F])))+)*\$".toRegex()
    val hexUnsignedString = "^([0](?i)(x)\\d{2}|[0](?i)(x)([0-7][a-fA-F]))\$".toRegex()
    val hexUnsignedListString = "^(^([0](?i)(x)\\d{2}|[0](?i)(x)([0-7][a-fA-F])))+(?: (([0](?i)(x)[0-7]{2}|[0](?i)(x)(\\d[a-fA-F])))+)*\$".toRegex()
    val binaryByteString = "^[0](?i)(b)\\d{8}$".toRegex()
    val binaryByteListString = "^(^[0](?i)(b)\\d{8})+(?: ([0](?i)(b)\\d{8})+)*\$".toRegex()
    val binaryUnsignedByteString = "^[0](?i)(b)[0]\\d{7}$".toRegex()
    val binaryUnsignedByteListString = "^(^[0](?i)(b)[0]\\d{7})+(?: ([0](?i)(b)[0]\\d{7})+)*\$".toRegex()
    val floatingString = "^\\d*\\.?\\d*\$".toRegex()//"^[-+]?([0-9]*.[0-9]+|[0-9]+)\$".toRegex()
    val onlyNumbersString = "^([0-9]+)\$".toRegex()

    fun checkFormatOfString(str: String): String? {
        var strReturn: String? = null
        if (str.isEmpty()) {
            println("Campo vacio")
            return null
        }
        when {
            isDecString(str) != null -> {
                println("Es DEC")
                strReturn = "DEC"
            }
            isHexString(str) != null -> {
                println("Es HEX")
                strReturn = "HEX"
            }
            isBinString(str) != null -> {
                println("Es BIN")
                strReturn = "BIN"
            }
            else -> {
                println("Es NADA")
            }
        }
        return strReturn
    }
    fun isDecString(str: String): String? {
        if(str.isEmpty()) {
            println("Sin texto para enviar al puerto COM")
            return null
        }
        val strList = str.split(" ")
        val strByteArray: ByteArray = ByteArray(strList.size)
        for ((j, i) in strList.withIndex()) {
            val regexp = integerString
            if(!i.contains(regexp)) {
                println("SDGSDGDS")
                return null
            } else {
                strByteArray[j] = i.toInt().toByte()
            }
        }
        return String(strByteArray)
    }

    fun isHexString(str: String): String? {
        if(str.isEmpty()) {
            println("Sin texto para enviar al puerto COM")
            return null
        }
        val strList = str.split(" ")
        val strByteArray: ByteArray = ByteArray(strList.size)
        for ((j, i) in strList.withIndex()) {
            val regexp = hexString
            if(!i.contains(regexp)) {
                return null
            } else {
                strByteArray[j] = i.toInt().toByte()
            }
        }
        return String(strByteArray)
    }

    fun isBinString(str: String): String? {
        if(str.isEmpty()) {
            println("Sin texto para enviar al puerto COM")
            return null
        }
        val strList = str.split(" ")
        val strByteArray: ByteArray = ByteArray(strList.size)
        for ((j, i) in strList.withIndex()) {
            val regexp = binaryByteString
            if(!i.contains(regexp)) {
                return null
            } else {
                strByteArray[j] = Integer.parseInt(i.subSequence(2, 10).toString(),2).toByte()
            }
        }
        return String(strByteArray)
    }
}