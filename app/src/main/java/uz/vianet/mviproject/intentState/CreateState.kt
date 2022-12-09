package uz.vianet.mviproject.intentState

import uz.vianet.mviproject.model.Post

sealed class CreateState {

    object Init:CreateState()

    data class CretePost(val post:Post):CreateState()

    data class Error(val error:String?):CreateState()
}