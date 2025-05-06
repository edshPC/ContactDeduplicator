package com.edsh.contdedser

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat

data class Contact(
    val id: Long,
    val name: String,
    val phones: List<String>,
    val emails: List<String>,
)

class ContactUtil(val applicationContext: Context) {
    val contentResolver: ContentResolver = applicationContext.contentResolver

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            throw IllegalStateException("Read contacts permission is denied")
        }
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            throw IllegalStateException("Write contacts permission is denied")
        }
    }

    fun getAllContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME
        )

        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                for (i in 0 until cursor.columnCount) {
                    Log.i("ServiceApp", "$i. ${cursor.getColumnName(i)} ${cursor.getType(i)}")
                }

                var i = 0
                val id = cursor.getLong(i++)

                val contact = Contact(
                    id = id,
                    name = cursor.getString(i++),
                    phones = getContactPhones(id),
                    emails = getContactEmalis(id),
                )

                contacts.add(contact)
            }
        }

        return contacts
    }

    private fun getContactPhones(contactId: Long): List<String> {
        val phones = mutableListOf<String>()

        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
            "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                phones.add(cursor.getString(0))
            }
        }

        return phones
    }

    private fun getContactEmalis(contactId: Long): List<String> {
        val emails = mutableListOf<String>()

        contentResolver.query(
            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Email.ADDRESS),
            "${ContactsContract.CommonDataKinds.Email.CONTACT_ID} = ?",
            arrayOf(contactId.toString()),
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                emails.add(cursor.getString(0))
            }
        }

        return emails
    }

}