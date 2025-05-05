import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edsh.service.IDedupService
import com.edsh.contdedup.state.SimpleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DedupViewModel : ViewModel() {

    private val _state = MutableStateFlow<SimpleState>(SimpleState(SimpleState.Status.IDLE))
    val state: StateFlow<SimpleState> = _state.asStateFlow()
    var dedupService: IDedupService? = null

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

    private suspend fun callAIDL() {
        val res = dedupService?.dedupContacts() ?: "Service not connected"
        if (res != "SUCCESS") throw RuntimeException(res)
    }

}