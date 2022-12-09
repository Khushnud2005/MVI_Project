package uz.vianet.mviproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.vianet.mviproject.databinding.ActivityDetailsBinding
import uz.vianet.mviproject.helper.MainHelperImpl
import uz.vianet.mviproject.intentState.DetailIntent
import uz.vianet.mviproject.intentState.DetailState
import uz.vianet.mviproject.network.RetrofitBuilder
import uz.vianet.mviproject.viewmodel.DetailViewModel
import uz.vianet.mviproject.viewmodel.ViewModelFactory

class DetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailsBinding
    lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val factory = ViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory)[DetailViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            val id = extras.getInt("id")
            intentLoadPost(id)
        }

        observeVM()

    }
    private fun observeVM() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is DetailState.Init ->{
                        Log.d("DetailsActivity","Init")
                    }
                    is DetailState.Loading ->{
                        Log.d("DetailsActivity","Loading")
                    }
                    is DetailState.LoadPost ->{

                        binding.tvTitle.setText(it.post.title.toUpperCase())
                        binding.tvBody.setText(it.post.body)
                        Log.d("DetailsActivity","AllPosts")
                    }
                    is DetailState.Error ->{
                        Log.d("DetailsActivity","Error"+ it)
                    }
                }
            }
        }
    }

    private fun intentLoadPost(id:Int) {
        lifecycleScope.launch {
            viewModel.id = id
            viewModel.detailIntent.send(DetailIntent.LoadPost)
        }
    }

}