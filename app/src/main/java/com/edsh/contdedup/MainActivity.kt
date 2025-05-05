package com.edsh.contdedup

import DedupViewModel
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.edsh.contdedup.component.DedupScreen
import com.edsh.contdedup.ui.theme.ContactDeduplicatorTheme
import com.edsh.service.IDedupService

class MainActivity : ComponentActivity() {

    private var dedupService: IDedupService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            dedupService = IDedupService.Stub.asInterface(service)
            dedupViewModel.dedupService = dedupService
            Log.i("App", "Successfully binded to AIDL service")
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            dedupService = null
        }
    }

    private val dedupViewModel by viewModels<DedupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactDeduplicatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DedupScreen(dedupViewModel)
                }
            }
        }
        bindToDedupService()
    }

    private fun bindToDedupService() {
        val intent = Intent().apply {
            component = ComponentName(
                "com.edsh.contdedser",  // пакет
                "com.edsh.contdedser.DedupService"  // имя класса
            )
        }
        val res = bindService(intent, connection, BIND_AUTO_CREATE)
        Log.i("App", "Binding result: $res")
    }

}

