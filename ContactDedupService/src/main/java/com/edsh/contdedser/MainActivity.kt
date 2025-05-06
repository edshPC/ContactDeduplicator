package com.edsh.contdedser

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.core.app.ActivityCompat


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Text(
                text = "Service started and ready to dedup your contacts"
            )
        }
        requestPermission(Manifest.permission.READ_CONTACTS)
        requestPermission(Manifest.permission.WRITE_CONTACTS)
        val serviceIntent = Intent(this, DedupService::class.java)
        startService(serviceIntent)
    }

    fun requestPermission(permission: String) {
        if (ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
    }

}
