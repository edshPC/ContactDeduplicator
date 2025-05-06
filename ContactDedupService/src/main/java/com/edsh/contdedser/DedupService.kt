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

    private lateinit var contactUtil: ContactUtil

    private val binder = object : IDedupService.Stub() {
        override fun dedupContacts(): String? = try {
            deduplicateContacts()
            "SUCCESS"
        } catch (e: Exception) {
            Log.e("ServiceApp", e.message, e)
            e.message ?: e.javaClass.simpleName
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        contactUtil = ContactUtil(this)
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
        contactUtil.checkPermissions()
        if (contactUtil.deduplicateContacts() == 0)
            throw IllegalStateException("Duplicate contacts not found")
    }
}
