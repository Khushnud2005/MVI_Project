package uz.vianet.mviproject.intentState

import uz.vianet.mviproject.model.Post

sealed class DetailState {

    object Init:DetailState()
    object Loading:DetailState()

    data class LoadPost(val post:Post):DetailState()
    data class Error(val error:String?):DetailState()
}