package com.lipisoft.toyshark

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

const val START_VPN = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prepareVpnService()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            START_VPN -> handleResult(resultCode)
        }
    }

    private fun handleResult(resultCode: Int) {
        when (resultCode) {
            Activity.RESULT_OK -> startService(Intent(this, ToysharkService::class.java))
            // TODO else -> retry
        }
    }

    private fun prepareVpnService() {
        startActivityForResult(VpnService.prepare(this), START_VPN)
    }
}
