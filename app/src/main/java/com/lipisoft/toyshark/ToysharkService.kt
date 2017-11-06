package com.lipisoft.toyshark

import android.app.Service
import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import java.io.FileInputStream

class ToysharkService : VpnService() {
    lateinit var vpnInterface : ParcelFileDescriptor
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        bringUpInterface()

        Thread(ReadInterface(vpnInterface)).start()

        return Service.START_STICKY
    }

    private fun bringUpInterface() {
        vpnInterface =  Builder().establish()
    }

    override fun onRevoke() {
        vpnInterface.close()
    }
}