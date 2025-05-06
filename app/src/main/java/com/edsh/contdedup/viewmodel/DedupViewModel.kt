package com.edsh.contdedup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edsh.contdedup.state.SimpleState
import com.edsh.service.IDedupService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel приложения, содержащее основную логику приложения.
 * Имеет внутри состояние [SimpleState].
 * Взаимодействует с интерфейсом [IDedupService] и обрабатывает результат
 */
class DedupViewModel : ViewModel() {

    private val _state = MutableStateFlow<SimpleState>(SimpleState(SimpleState.Status.IDLE))
    val state: StateFlow<SimpleState> = _state.asStateFlow()
    var dedupService: IDedupService? = null

    /**
     * Обработчик нажатия на кнопку "Start".
     * Устанавливает состояние в зависимости от результата
     */
    fun onButtonClicked() {
        viewModelScope.launch {
            _state.value = SimpleState(SimpleState.Status.LOADING)
            try {
                callAIDL()
                _state.value = SimpleState(SimpleState.Status.SUCCESS)
            } catch (e: Exception) {
                _state.value = SimpleState(SimpleState.Status.ERROR, e.message)
            }
            delay(5000)
            _state.value = SimpleState(SimpleState.Status.IDLE)
        }
    }

    /**
     * Вызов сервиса через AIDL
     */
    private suspend fun callAIDL() {
        val res = dedupService?.dedupContacts() ?: "Service not connected"
        if (res != "SUCCESS") throw RuntimeException(res)
    }

}