package com.lipisoft.toyshark.com.lipisoft.toyshark.tcp

data class Options(val maximumSegmentSize : Int, val windowScale : Int,
                   val selectiveAcknowledgementPermitted : Boolean,
                   val selectiveAcknowledgements: List<SelectiveAcknowledgement>,
                   val timeStamp: TimeStamp)