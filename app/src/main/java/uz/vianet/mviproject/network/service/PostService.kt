package uz.vianet.mviproject.network.service

import retrofit2.Call
import retrofit2.http.*
import uz.vianet.mviproject.model.Post

interface PostService {

    @Headers(
        "Content-type:application/json"
    )

    @GET("posts")
    suspend fun listPost(): ArrayList<Post>

    @GET("posts/{id}")
    suspend fun detailPost(@Path("id") id: Int): Post

    @POST("posts")
    suspend fun createPost(@Body post: Post): Post

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Post

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Post
}