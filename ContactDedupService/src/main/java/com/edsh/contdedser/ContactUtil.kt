package com.edsh.contdedser

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.util.Log
import androidx.core.content.ContextCompat

/**
 * Представление данных о контакте.
 * Может повторяться в нескольких разных контактах
 *
 * @param name Имя (+ Фамилия) контакта
 * @param phones Список телефонных номеров
 * @param emails Список Email-адересов
 */
data class ContactData(
    val name: String,
    val phones: List<String>,
    val emails: List<String>
)

/**
 * Представление контакта.
 * *Не* может повторяться даже у одинаковых контактов
 *
 * @param id ID контакта в android
 * @param data Задаваемые пользователем данные контакта
 */
data class Contact(
    val id: Long,
    val data: ContactData
)

/**
 * Утилита для работы с контактами
 *
 * @param applicationContext Контекст приложения, в котором создается класс
 */
class ContactUtil(val applicationContext: Context) {
    val contentResolver: ContentResolver = applicationContext.contentResolver

    /**
     * Проверка на наличие необходимых прав для работы с контактами
     *
     * @throws IllegalStateException Если права не удовлетворяют требуемым
     */
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

    /**
     * Очищает все повторяющиеся контакты
     *
     * @return Количество очищенных контактов
     */
    fun deduplicateContacts(): Int {
        var count = 0
        val seen = mutableSetOf<ContactData>()
        for (contact in getAllContacts()) {
            if (!seen.add(contact.data)) { // удаляем, если повторяется
                removeContact(contact.id)
                count++
            }
        }
        return count
    }

    /**
     * Получить все доступные контакты пользователя
     *
     * @return Список представлений контактов
     */
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
                    data = ContactData(
                        name = cursor.getString(i++),
                        phones = getContactPhones(id),
                        emails = getContactEmalis(id),
                    )
                )

                contacts.add(contact)
            }
        }

        return contacts
    }

    /**
     * Удаляет контакт
     *
     * @param contactId ID контакта в android для удаления
     */
    fun removeContact(contactId: Long) {
        val uri = ContentUris.withAppendedId(
            ContactsContract.Contacts.CONTENT_URI,
            contactId
        )
        contentResolver.delete(uri, null, null)
    }

    /**
     * Получить все номера телефонов контакта
     *
     * @param contactId ID контакта в android
     * @return Список номеров телефонов
     */
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

    /**
     * Получить все Email-адреса контакта
     *
     * @param contactId ID контакта в android
     * @return Список Email-адресов
     */
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