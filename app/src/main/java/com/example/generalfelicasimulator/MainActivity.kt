package com.example.generalfelicasimulator

import android.app.AlertDialog
import android.content.ComponentName
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.nfc.cardemulation.NfcFCardEmulation
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var nfcFCardEmulation: NfcFCardEmulation
    private lateinit var myComponentName: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION_NFCF)) {
            Log.e("GeneralFelicaSimulator", "HCE-F is not supported")
            AlertDialog.Builder(ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog))
                .setTitle("Error").setMessage("HCE-F is not supported").show()
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcFCardEmulation = NfcFCardEmulation.getInstance(nfcAdapter)
        myComponentName = ComponentName(
            "com.example.generalfelicasimulator",
            "com.example.generalfelicasimulator.HCEFService"
        )

        nfcFCardEmulation.setNfcid2ForService(myComponentName,"02FE000000000000")
        nfcFCardEmulation.registerSystemCodeForService(myComponentName,"4000")
    }

    override fun onResume() {
        super.onResume()
        nfcFCardEmulation.enableService(this, myComponentName)
    }

    override fun onPause() {
        super.onPause()
        nfcFCardEmulation.disableService(this)
    }
}