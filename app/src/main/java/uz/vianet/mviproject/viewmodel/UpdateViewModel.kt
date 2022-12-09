package uz.vianet.mviproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import uz.vianet.mviproject.intentState.*
import uz.vianet.mviproject.model.Post

import uz.vianet.mviproject.repository.PostRepository

class UpdateViewModel(private val repository: PostRepository) : ViewModel() {

    val updateIntent = Channel<UpdateIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<UpdateState>(UpdateState.Init)
    val state: StateFlow<UpdateState> get() = _state
    lateinit var post: Post
    var id:Int = 0
    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            updateIntent.consumeAsFlow().collect {
                when (it) {
                    is UpdateIntent.LoadPost -> apiLoadPost()
                    is UpdateIntent.UpdatePost -> apiUpdatePost()
                }
            }
        }
    }


    private fun apiLoadPost() {
        viewModelScope.launch {
            _state.value = UpdateState.Loading
            _state.value = try {
                UpdateState.LoadPost(repository.loadPost(id))
            } catch (e: Exception) {
                UpdateState.Error(e.localizedMessage)
            }
        }
    }

    private fun apiUpdatePost() {
        viewModelScope.launch {
            _state.value = UpdateState.Loading
            _state.value = try {
                UpdateState.UpdatePost(repository.updatePost(id,post))
            } catch (e: Exception) {
                UpdateState.Error(e.localizedMessage)
            }
        }
    }

}