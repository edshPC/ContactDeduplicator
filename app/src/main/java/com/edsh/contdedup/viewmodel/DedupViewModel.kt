import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edsh.contdedup.state.SimpleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DedupViewModel : ViewModel() {

    private val _state = MutableStateFlow<SimpleState>(SimpleState(SimpleState.Status.IDLE))
    val state: StateFlow<SimpleState> = _state.asStateFlow()

    fun onButtonClicked() {
        viewModelScope.launch {
            _state.value = SimpleState(SimpleState.Status.LOADING)
            try {
                val result = fetchData() // Имитация долгой операции
                _state.value = SimpleState(SimpleState.Status.SUCCESS)
            } catch (e: Exception) {
                _state.value = SimpleState(SimpleState.Status.ERROR)
            }
            delay(5000)
            _state.value = SimpleState(SimpleState.Status.IDLE)
        }
    }

    private suspend fun fetchData(): String {
        delay(2000) // Задержка 2 сек (например, запрос к API)
        return "Данные успешно загружены!"
    }

}