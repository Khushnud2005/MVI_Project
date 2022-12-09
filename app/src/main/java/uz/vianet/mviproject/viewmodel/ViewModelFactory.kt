package uz.vianet.mviproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.vianet.mviproject.helper.MainHelper
import uz.vianet.mviproject.repository.PostRepository

class ViewModelFactory(private val mainHelper: MainHelper) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(PostRepository(mainHelper)) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(PostRepository(mainHelper)) as T
        }
        if (modelClass.isAssignableFrom(CreateViewModel::class.java)){
            return CreateViewModel(PostRepository(mainHelper)) as T
        }
        if (modelClass.isAssignableFrom(UpdateViewModel::class.java)){
            return UpdateViewModel(PostRepository(mainHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}