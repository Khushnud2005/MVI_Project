package uz.vianet.mviproject.helper


import uz.vianet.mviproject.model.Post
import uz.vianet.mviproject.network.service.PostService

class MainHelperImpl (private val postService: PostService): MainHelper {

    override suspend fun allPosts(): ArrayList<Post> {
        return postService.listPost()
    }

    override suspend fun deletePost(id: Int): Post {
        return postService.deletePost(id)
    }

    override suspend fun createPost(post: Post): Post {
        return postService.createPost(post)
    }

    override suspend fun updatePost(id: Int, post: Post): Post {
        return postService.updatePost(id,post)
    }

    override suspend fun loadPost(id: Int): Post {
        return postService.detailPost(id)
    }
}