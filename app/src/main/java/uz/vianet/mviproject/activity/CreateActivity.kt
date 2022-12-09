package uz.vianet.mviproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.vianet.mviproject.databinding.ActivityCreateBinding
import uz.vianet.mviproject.helper.MainHelperImpl
import uz.vianet.mviproject.intentState.CreateIntent
import uz.vianet.mviproject.intentState.CreateState
import uz.vianet.mviproject.model.Post
import uz.vianet.mviproject.network.RetrofitBuilder
import uz.vianet.mviproject.utils.Utils.toast
import uz.vianet.mviproject.viewmodel.CreateViewModel
import uz.vianet.mviproject.viewmodel.ViewModelFactory

class CreateActivity : AppCompatActivity() {
    lateinit var binding: ActivityCreateBinding
    lateinit var viewModel: CreateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        val factory = ViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory)[CreateViewModel::class.java]

        binding.btnSubmit.setOnClickListener{ newPost()}

        observeVM()
    }

    private fun newPost() {
        val title: String = binding.etTitle.getText().toString()
        val body: String = binding.etBody.getText().toString().trim { it <= ' ' }
        val id_user: String = binding.etUserId.getText().toString().trim { it <= ' ' }
        if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
            val post = Post(id_user.toInt(), title, body)
            intentCreatePost(post)
        }
    }

    private fun observeVM() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is CreateState.Init ->{
                        Log.d("CreateActivity","Init")
                    }
                    is CreateState.CretePost ->{
                        toast(this@CreateActivity,"New Post Created")
                        Log.d("CreateActivity","CreatePost")
                        val intent = Intent()
                        intent.putExtra("title", it.post.title)
                        setResult(RESULT_OK, intent)
                        super@CreateActivity.onBackPressed()
                    }
                    is CreateState.Error ->{
                        it
                        Log.d("CreateActivity","Error"+ it)
                    }
                }
            }
        }
    }
    fun intentCreatePost(post: Post){
        lifecycleScope.launch {
            viewModel.post = post
            viewModel.createIntent.send(CreateIntent.CreatePost)
        }
    }
}