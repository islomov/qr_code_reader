package com.epam.qrcodereader.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.epam.qrcodereader.R
import com.epam.qrcodereader.ui.button.ButtonFragment
import com.epam.qrcodereader.ui.qrResult.QrResultFragment
import com.epam.qrcodereader.ui.qrScanner.QrScannerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openButton()
    }


    fun openButton() {
        replace(ButtonFragment.newInstance())
    }

    fun openScanner() {
        replace(QrScannerFragment.newInstance())
    }

    fun openScannerResult(result: String?) {
        replace(QrResultFragment.newInstance(result))
    }

    fun replace(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}