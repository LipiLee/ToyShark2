package com.lipisoft.toyshark

import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.nio.ByteBuffer

class ReadInterface(val vpnInterface : ParcelFileDescriptor) : Runnable {
    val buffer = ByteBuffer.allocate(1500)

    override fun run() {
        val inputStream = FileInputStream(vpnInterface.fileDescriptor)

        do {
            val size = inputStream.read(buffer.array())

            if (size > 0) {
                val fittedBuffer = ByteArray(size)
                buffer.limit(size)
                buffer.put(fittedBuffer)
                buffer.clear()
            }
        } while (size >= 0)
    }
}