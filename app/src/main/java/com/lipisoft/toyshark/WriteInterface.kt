package com.lipisoft.toyshark

import android.os.ParcelFileDescriptor
import java.io.FileOutputStream
import java.nio.ByteBuffer

class WriteInterface(val vpnInterface : ParcelFileDescriptor) : Runnable {
    val buffer = ByteBuffer.allocate(Int.MAX_VALUE)

    override fun run() {
        val OutputStream = FileOutputStream(vpnInterface.fileDescriptor)

        while (true) {
            // TODO get byte stream from SessionHandler

            OutputStream.write(buffer.array())
            buffer.clear()
        }
    }
}