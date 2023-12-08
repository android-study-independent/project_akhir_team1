package com.example.tanify.ui.bottomNav.forum.detailDiscuss

import android.content.Context
import android.content.SharedPreferences
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.forum.ForumByIdResponse
import com.example.tanify.databinding.ActivityDetailDiscussBinding
import com.example.tanify.ui.bottomNav.forum.detailDiscuss.item.ItemCommentAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tanify.R
import com.example.tanify.data.data.CommentData
import com.example.tanify.data.response.forum.CommentResponse
import com.example.tanify.helper.formatDate
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class DetailDiscussActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDiscussBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var commentAdapter: ItemCommentAdapter

    companion object {
        private const val TAG = "DetailDiscussActivity"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
        private var ID: Int = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDiscussBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        ID = intent.getIntExtra("ID_FORUM", 0)
        setView()
        setAction()
        getForumById(ID)
        setRecyclerViewComment()
    }

    private fun setView(){
        binding.edComment.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.btnKirim.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_IN)
            } else {
                binding.btnKirim.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.SRC_IN)
            }
        }
    }

    private fun setForumData(nama: String, tanggal: String, judul: String, kontenForum: String, pathProfile: String, pathPoster: String) {
        binding.tvNameCreatorForum.text = nama
        binding.tvTanggalForum.text = tanggal
        binding.tvJudulForum.text = judul
        binding.tvIsiKontenForum.text = kontenForum

        Glide.with(this)
            .load(pathProfile)
            .placeholder(R.drawable.bg_load_profile)
            .into(binding.ivProfileForum)

        Glide.with(this)
            .load(pathPoster)
            .placeholder(R.drawable.bg_load_poster_forum)
            .into(binding.ivPosterForum)
    }

    private fun setAction(){
        binding.btnKirim.setOnClickListener {
            val comment = binding.edComment.text.toString()
            postComment(ID, comment)
            binding.edComment.setText("")
            hideKeyboard()
        }
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setRecyclerViewComment(){
        commentAdapter = ItemCommentAdapter(
            this,
            emptyList(),
        ) {
            Toast.makeText(this, "like komen", Toast.LENGTH_SHORT).show()
        }
        binding.rvComment.layoutManager = LinearLayoutManager(this)
        binding.rvComment.adapter = commentAdapter
    }

    private fun getForumById(id: Int){
        ApiConfig.instanceRetrofit.getDetailForum(
            id,
            "Bearer " + TOKEN
        ).enqueue(object : Callback<ForumByIdResponse>{
            override fun onResponse(
                call: Call<ForumByIdResponse>,
                response: Response<ForumByIdResponse>
            ) {
                if (response.isSuccessful) {
                    val currentForum = response.body()
                    if (currentForum != null) {
                        Log.d(TAG, "onSuccess: $currentForum")
                        val dataForum = currentForum.data
                        setForumData(
                            dataForum?.createdBy?.nama!!,
                            formatDate(dataForum.createdAt.toString()),
                            dataForum.title ?: "",
                            dataForum.content ?: "",
                            dataForum.createdBy.photo ?: "",
                            dataForum.cover ?: ""
                        )
                        if (dataForum.comment != null){
                            commentAdapter.updateDataComment(currentForum.data.comment)
                        } else {
                            Log.d(TAG, "Data kosong")
                        }
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<ForumByIdResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun postComment(id: Int, comment: String) {
        val data = CommentData(comment)
        ApiConfig.instanceRetrofit.postComment(
            id,
            "Bearer " + TOKEN,
            data
        ).enqueue(object : Callback<CommentResponse>{
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "onSuccess: ${response.message()}")
                    getForumById(ID)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showSnackbar("Failed to post comment")
                }
            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                showSnackbar("Failed to post comment")
            }
        })
    }

    private fun hideKeyboard() {
        val view: View? = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showSnackbar(message: String) {
        val rootView: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }
}