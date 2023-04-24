package com.adamve.hwkeyinfo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.adamve.hwkeyinfo.ui.theme.HwKeyInfoTheme

class HwKeyInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HwKeyInfoTheme {
                HwKeyInfoApp()
            }
        }
    }
}
