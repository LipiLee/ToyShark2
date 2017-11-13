package com.lipisoft.toyshark.com.lipisoft.toyshark.tcp

data class ControlBits(val urg : Boolean, val ack : Boolean, val psh : Boolean,
                       val rst : Boolean, val syn : Boolean, val fin : Boolean)
