package uz.vianet.mviproject.repository

import uz.vianet.mviproject.helper.MainHelper
import uz.vianet.mviproject.model.Post

class PostRepository(private val helper: MainHelper) {

    suspend fun allPosts() = helper.allPosts()
    suspend fun delPost(id:Int) = helper.deletePost(id)
    suspend fun createPost(post: Post) = helper.createPost(post)
    suspend fun updatePost(id:Int,post: Post) = helper.updatePost(id,post)
    suspend fun loadPost(id: Int) = helper.loadPost(id)


}