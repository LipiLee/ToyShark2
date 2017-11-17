package com.lipisoft.toyshark.transport.udp

import java.nio.ByteBuffer

data class Udp(val sourcePort : Short, val destinationPort : Short,
               val length : Short, val checksum : Short)

fun extractor(stream : ByteBuffer) : Udp {
    return Udp(stream.short, stream.short, stream.short, stream.short)
}
