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
            Activity.RESULT_OK -> startService(Intent(this, ToySharkService::class.java))
            // TODO else -> retry
        }
    }

    private fun prepareVpnService() {
        val intent = VpnService.prepare(this)

        if (intent != null)
            startActivityForResult(intent, START_VPN)
        else
            onActivityResult(START_VPN, Activity.RESULT_OK, null)
    }
}
