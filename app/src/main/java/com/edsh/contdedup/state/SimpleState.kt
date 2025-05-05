package com.edsh.contdedup.state

data class SimpleState(
    var status: Status,
    var error: String? = null,
) {
    enum class Status {
        IDLE, LOADING, SUCCESS, ERROR
    }
}
