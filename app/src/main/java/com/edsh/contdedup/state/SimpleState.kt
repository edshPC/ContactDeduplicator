package com.edsh.contdedup.state

/**
 * Состояние приложения
 * @param status Непосредственно состояние
 * @param error Сообщение об ошибке
 */
data class SimpleState(
    var status: Status,
    var error: String? = null,
) {
    enum class Status {
        /**
         * Приложение ничего не делает, и ждет действие от пользователя
         */
        IDLE,

        /**
         * Приложение обрабатывает действие
         */
        LOADING,

        /**
         * Действие успешно выполнено
         */
        SUCCESS,

        /**
         * Действие выполнено с ошибкой
         */
        ERROR
    }
}
