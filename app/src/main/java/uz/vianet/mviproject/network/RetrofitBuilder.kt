package uz.vianet.mviproject.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.vianet.mviproject.network.service.PostService

object RetrofitBuilder {

    private val IS_TESTER = true
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"


    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val POST_SERVICE: PostService = getRetrofit().create(PostService::class.java)
}