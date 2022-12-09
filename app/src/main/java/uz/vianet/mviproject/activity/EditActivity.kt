package uz.vianet.mviproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.vianet.mviproject.databinding.ActivityEditBinding
import uz.vianet.mviproject.helper.MainHelperImpl
import uz.vianet.mviproject.intentState.UpdateIntent
import uz.vianet.mviproject.intentState.UpdateState
import uz.vianet.mviproject.model.Post
import uz.vianet.mviproject.network.RetrofitBuilder
import uz.vianet.mviproject.viewmodel.UpdateViewModel
import uz.vianet.mviproject.viewmodel.ViewModelFactory

class EditActivity : AppCompatActivity() {
    var id: Int = 0
    lateinit var binding: ActivityEditBinding
    lateinit var viewModel: UpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }
    fun initViews(){
        val factory = ViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory)[UpdateViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            Log.d("###", "extras not NULL - ")
            id = extras.getInt("id")
        }
        intentLoadPost(id)
        observeVM()
        binding.btnSubmit.setOnClickListener {editPost()   }
    }

    private fun observeVM() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is UpdateState.Init ->{
                        Log.d("EditActivity","Init")
                    }
                    is UpdateState.Loading ->{

                        Log.d("EditActivity","Loading")
                    }
                    is UpdateState.LoadPost ->{
                        it
                        binding.etUserId.setText(it.post.id.toString())
                        binding.etTitle.setText(it.post.title)
                        binding.etText.setText(it.post.body)
                        Log.d("EditActivity","LoadPost")
                    }
                    is UpdateState.UpdatePost ->{
                        val intent = Intent(this@EditActivity, MainActivity::class.java)
                        intent.putExtra("title", it.post.title)
                        startActivity(intent)
                        Log.d("EditActivity","UpdatePost")
                    }
                    is UpdateState.Error ->{
                        it
                        Log.d("EditActivity","Error"+ it)

                    }
                }
            }
        }
    }

    private fun intentLoadPost(id:Int) {
        lifecycleScope.launch {
            viewModel.id = id
            viewModel.updateIntent.send(UpdateIntent.LoadPost)
        }
    }

    fun intentUpdatePost(id:Int,post:Post){
        lifecycleScope.launch {
            viewModel.id = id
            viewModel.post = post
            viewModel.updateIntent.send(UpdateIntent.UpdatePost)
        }
    }

    private fun editPost() {
        val title = binding.etTitle.text.toString()
        val body = binding.etText.text.toString().trim { it <= ' ' }
        val id_user = binding.etUserId.text.toString().trim { it <= ' ' }

        if (title.isNotEmpty() && body.isNotEmpty() && id_user.isNotEmpty()){
            val post = Post(id.toInt(),id_user.toInt(), title, body)
            Log.d("@@EditActivity","Pot model Created")
            intentUpdatePost(id, post)
        }
    }
}