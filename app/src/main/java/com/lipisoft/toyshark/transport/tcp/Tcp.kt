package com.lipisoft.toyshark.transport.tcp

import java.nio.ByteBuffer

data class Tcp(val sourcePort : Short, val destinationPort : Short, val sequenceNumber : Int,
               val acknowledgmentNumber : Int, val dataOffset : Int,
               val controlBits: ControlBits, val windowSize : Short, val checksum : Short,
               val urgentPointer : Short, val options: Options?)

data class ControlBits(val urg : Boolean, val ack : Boolean, val psh : Boolean,
                       val rst : Boolean, val syn : Boolean, val fin : Boolean)

data class Options(val maximumSegmentSize : Short, val windowScale : Byte,
                   val selectiveAcknowledgementPermitted : Boolean,
                   val selectiveAcknowledgements: List<SelectiveAcknowledgement>,
                   val timeStamp: TimeStamp)
enum class Option(val kind: Int) {
    END(0), NOP(1), MSS(2), WINDOW_SCALE(3), SELECTIVE_ACK_PERMITTED(4), SELECTIVE_ACK(5), TIMESTAMP(8)
}
data class SelectiveAcknowledgement(val begin : Int, val end : Int)

data class TimeStamp(val timeStamp : Int, val echoOfPreviousTimeStamp : Int)

fun extractor(stream: ByteBuffer) : Tcp {
    val sourcePort = stream.short
    val destinationPort = stream.short
    val sequenceNumber = stream.int
    val acknowledgmentNumber = stream.int
    val dataOffset = (stream.get().toInt() shr 4) and 0xf
    val controlBits = extractControlFlags(stream)
    val window = stream.short
    val checksum = stream.short
    val urgentPointer = stream.short
    var options : Options? = null

    if (dataOffset > 5) {
        options = extractOptions(stream, (dataOffset - 5) * 4 )
    }

    return Tcp(sourcePort, destinationPort, sequenceNumber, acknowledgmentNumber, dataOffset, controlBits, window, checksum, urgentPointer, options)
}

fun extractControlFlags(stream: ByteBuffer) : ControlBits {
    val value = stream.get().toInt()
    val urg = (value and 0b100000) == 0b100000
    val ack = (value and 0b10000) == 0b10000
    val psh = (value and 0b1000) == 0b1000
    val rst = (value and 0b100) == 0b100
    val syn = (value and 0b10) == 0b10
    val fin = (value and 0b1) == 0b1

    return ControlBits(urg, ack, psh, rst, syn, fin)
}

fun extractOptions(stream: ByteBuffer, totalLengthBytes: Int) : Options {
    var remainingBytes = totalLengthBytes
    var maximumSegmentSize : Short = 0
    var windowScale : Byte = 0
    var selectiveAcknowledgement = false
    var list : MutableList<SelectiveAcknowledgement> = mutableListOf()
    var timestamp = TimeStamp(0, 0)

    do {
        val kind = stream.get().toInt(); remainingBytes--
        if (kind < Option.MSS.kind) {
            val length = stream.get().toInt(); remainingBytes--

            when (kind) {
                Option.MSS.kind -> {
                    maximumSegmentSize = stream.short
                    remainingBytes -= 2
                }
                Option.WINDOW_SCALE.kind -> {
                    windowScale = stream.get()
                    remainingBytes--
                }
                Option.SELECTIVE_ACK_PERMITTED.kind -> selectiveAcknowledgement = true
                Option.SELECTIVE_ACK.kind -> {
                    list = extractSelectiveAcknowledgement(stream, length)
                    remainingBytes -= (length - 2)
                }
                Option.TIMESTAMP.kind -> {
                    timestamp = TimeStamp(stream.int, stream.int)
                    remainingBytes -= 8
                }
                else -> remainingBytes -= (length - 2)
            }
        }
    } while (kind != Option.END.kind && remainingBytes > 0)

    return Options(maximumSegmentSize, windowScale, selectiveAcknowledgement, list.toList(), timestamp)
}

fun extractSelectiveAcknowledgement(stream: ByteBuffer, length: Int) : MutableList<SelectiveAcknowledgement> {
    val list: MutableList<SelectiveAcknowledgement> = mutableListOf()
    val count = (length - 2) / 4
    var i = 0

    while (i++ < count) {
        list.add(SelectiveAcknowledgement(stream.int, stream.int))
    }

    return list
}
