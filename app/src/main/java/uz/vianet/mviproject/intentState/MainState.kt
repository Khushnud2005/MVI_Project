package uz.vianet.mviproject.intentState

import uz.vianet.mviproject.model.Post

sealed class MainState {

    object Init:MainState()
    object Loading:MainState()

    data class AllPosts(val posts:ArrayList<Post>):MainState()
    data class DelPost(val post:Post):MainState()

    data class Error(val error:String?):MainState()
}