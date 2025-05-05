package com.edsh.contdedser

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(
                text = "Service started and ready to dedup your contacts"
            )
        }
        val serviceIntent = Intent(this, DedupService::class.java)
        startService(serviceIntent)
    }

}
