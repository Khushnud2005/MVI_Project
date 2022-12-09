package uz.vianet.mviproject.intentState

import uz.vianet.mviproject.model.Post

sealed class UpdateState {

    object Init:UpdateState()
    object Loading:UpdateState()

    data class LoadPost(val post:Post):UpdateState()
    data class UpdatePost(val post:Post):UpdateState()

    data class Error(val error:String?):UpdateState()
}