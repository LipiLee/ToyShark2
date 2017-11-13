package com.lipisoft.toyshark

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import java.io.FileInputStream

class ToySharkService : VpnService() {
    lateinit var vpnInterface : ParcelFileDescriptor
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bringUpInterface()

        Thread(ReadInterface(vpnInterface)).start()
//        Thread(WriteInterface(vpnInterface)).start()

        return Service.START_STICKY
    }

    private fun bringUpInterface() {
        val builder =  Builder()
                .addAddress("192.168.0.1", 32)
                .addRoute("0.0.0.0", 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder.setBlocking(true)

        vpnInterface = builder.establish()
    }

    override fun onRevoke() {
        vpnInterface.close()
    }
}