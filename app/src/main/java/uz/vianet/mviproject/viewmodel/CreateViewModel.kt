package uz.vianet.mviproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import uz.vianet.mviproject.intentState.CreateIntent
import uz.vianet.mviproject.intentState.CreateState
import uz.vianet.mviproject.model.Post

import uz.vianet.mviproject.repository.PostRepository

class CreateViewModel(private val repository: PostRepository) : ViewModel() {

    val createIntent = Channel<CreateIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<CreateState>(CreateState.Init)
    val state: StateFlow<CreateState> get() = _state
    lateinit var post: Post
    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            createIntent.consumeAsFlow().collect {
                when (it) {
                    is CreateIntent.CreatePost -> apiCreatePost()
                }
            }
        }
    }


    private fun apiCreatePost() {
        viewModelScope.launch {
            _state.value = try {
                CreateState.CretePost(repository.createPost(post))
            } catch (e: Exception) {
                CreateState.Error(e.localizedMessage)
            }
        }
    }

}