package uz.vianet.mviproject.intentState

sealed class UpdateIntent {

    object LoadPost: UpdateIntent()
    object UpdatePost: UpdateIntent()
}