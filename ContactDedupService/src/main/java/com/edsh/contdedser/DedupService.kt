package com.edsh.contdedser

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.edsh.service.IDedupService


class DedupService : Service() {
    companion object {
        var isRunning = false
    }

    private val binder = object : IDedupService.Stub() {
        override fun dedupContacts(): String? = try {
            deduplicateContacts()
            "SUCCESS"
        } catch (e: Exception) {
            e.message
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        isRunning = true
        Log.i("ServiceApp", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("ServiceApp", "Service started: ${intent?.component}")
        return START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        super.onDestroy()
        Log.i("ServiceApp", "Service destroyed")
    }

    fun deduplicateContacts() {
        throw RuntimeException("Hello world")
    }
}
