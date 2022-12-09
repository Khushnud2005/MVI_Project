package uz.vianet.mviproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import uz.vianet.mviproject.adapter.PostAdapter
import uz.vianet.mviproject.databinding.ActivityMainBinding
import uz.vianet.mviproject.helper.MainHelperImpl
import uz.vianet.mviproject.intentState.MainIntent
import uz.vianet.mviproject.intentState.MainState
import uz.vianet.mviproject.model.Post
import uz.vianet.mviproject.network.RetrofitBuilder
import uz.vianet.mviproject.utils.Utils.toast
import uz.vianet.mviproject.viewmodel.MainViewModel
import uz.vianet.mviproject.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.pbLoading.visibility = View.VISIBLE
        val factory = ViewModelFactory(MainHelperImpl(RetrofitBuilder.POST_SERVICE))
        viewModel = ViewModelProvider(this,factory).get(MainViewModel::class.java)

        binding.recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.floating.setOnClickListener { openCreateActivity() }
        observeVM()
        intentAllPosts()

        val extras = intent.extras
        if (extras != null) {
            binding.pbLoading.visibility = View.VISIBLE
            Toast.makeText(this@MainActivity,"${extras.getString("title")} Updated", Toast.LENGTH_LONG).show()

        }
    }

    private fun openCreateActivity() {
        val intent = Intent(this@MainActivity, CreateActivity::class.java)
        launchCreateActivity.launch(intent)
    }
    var launchCreateActivity = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.pbLoading.visibility = View.VISIBLE
            if (result.data != null) {
                val title = result.data!!.getStringExtra("title")
                toast(this@MainActivity,"$title Created")
                intentAllPosts()
            }
        } else {
            Toast.makeText(this@MainActivity, "Operation canceled", Toast.LENGTH_LONG).show()
        }
    }
    private fun observeVM() {
        lifecycleScope.launch {
            viewModel.state.collect{
                when(it){
                    is MainState.Init ->{
                        Log.d("MainActivity","Init")
                    }
                    is MainState.Loading ->{
                        Log.d("MainActivity","Loading")
                    }
                    is MainState.AllPosts ->{
                        refreshAdapter(it.posts)
                        binding.pbLoading.visibility = View.GONE
                        Log.d("MainActivity","AllPosts")
                    }
                    is MainState.DelPost ->{
                        intentAllPosts()
                        Log.d("MainActivity","Delete Post")
                        toast(this@MainActivity,"Post deleted")
                    }
                    is MainState.Error ->{
                        it
                        Log.d("MainActivity","Error"+ it)
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }
        }
    }
    private fun refreshAdapter(posts:ArrayList<Post>){
        val adapter = PostAdapter(this,posts)
        binding.recyclerView.adapter = adapter
    }

    private fun intentAllPosts() {
        lifecycleScope.launch {
            viewModel.mainIntent.send(MainIntent.AllPosts)
        }
    }

    fun intentDeletePost(id:Int){
        lifecycleScope.launch {
            viewModel.postId = id
            viewModel.mainIntent.send(MainIntent.DelPost)
        }
    }
}