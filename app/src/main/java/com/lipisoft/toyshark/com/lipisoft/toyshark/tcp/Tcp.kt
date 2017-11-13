package com.lipisoft.toyshark.com.lipisoft.toyshark.tcp

import com.lipisoft.toyshark.com.lipisoft.toyshark.transport.Transport

data class Tcp(sourcePort, destinationPort, val sequenceNumber : Long,
               val acknowledgmentNumber : Long, val dataOffset : Int,
               val controlBits: ControlBits, val windowSize : Int, val checksum : Int,
               val urgentPointer : Int, val options: Options) : Transport(sourcePort, destinationPort)