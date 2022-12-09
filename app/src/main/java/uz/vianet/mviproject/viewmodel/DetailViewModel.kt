package uz.vianet.mviproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import uz.vianet.mviproject.intentState.*

import uz.vianet.mviproject.repository.PostRepository

class DetailViewModel(private val repository: PostRepository) : ViewModel() {

    val detailIntent = Channel<DetailIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<DetailState>(DetailState.Init)
    val state: StateFlow<DetailState> get() = _state

    var id:Int = 0
    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            detailIntent.consumeAsFlow().collect {
                when (it) {
                    is DetailIntent.LoadPost -> apiLoadPost()
                }
            }
        }
    }


    private fun apiLoadPost() {
        viewModelScope.launch {
            _state.value = DetailState.Loading
            _state.value = try {
                DetailState.LoadPost(repository.loadPost(id))
            } catch (e: Exception) {
                DetailState.Error(e.localizedMessage)
            }
        }
    }



}