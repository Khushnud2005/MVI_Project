package uz.vianet.mviproject.intentState

sealed class MainIntent {

    object AllPosts: MainIntent()
    object DelPost: MainIntent()
}