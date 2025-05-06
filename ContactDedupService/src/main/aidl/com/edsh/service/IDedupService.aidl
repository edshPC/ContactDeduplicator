// IDedupService.aidl
package com.edsh.service;

// Declare any non-default types here with import statements

/**
* Интерфейс взаимодействия с сервисом по удалению повторяющихся контактов
*/
interface IDedupService {
    /**
    * Удаляет повторяющиеся контакты
    *
    * @return "SUCCESS", если повторяющиеся контакты удалены корректно, иначе сообщение об ошибке
    */
    String dedupContacts();
}
