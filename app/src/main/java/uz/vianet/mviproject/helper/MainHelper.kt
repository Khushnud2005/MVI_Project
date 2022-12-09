package uz.vianet.mviproject.helper

import uz.vianet.mviproject.model.Post


interface MainHelper {
    suspend fun allPosts():ArrayList<Post>
    suspend fun deletePost(id: Int):Post
    suspend fun createPost(post: Post):Post
    suspend fun updatePost(id: Int,post: Post):Post
    suspend fun loadPost(id: Int):Post
}