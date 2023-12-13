package com.example.tanify.ui.lms.ListSearchLesson

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.lms.LmsDto
import com.example.tanify.data.response.lms.ModulItem
import com.example.tanify.data.response.lms.searchResponse
import com.example.tanify.databinding.ActivityListSearchBinding
import com.example.tanify.databinding.ActivityLmsBinding
import com.example.tanify.ui.lms.LmsActivity
import com.example.tanify.ui.lms.adapterLessson.lessonAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListSearchBinding
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var adapterLms: lessonAdapter
    private lateinit var recyclerviewSearch: RecyclerView
    private var listModul: List<ModulItem>? = null

    companion object {
        private const val TAG = "lmsHome"
        private var TOKEN = "token"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // sharedPreferences data
        sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        //  set recycleview
        recyclerviewSearch = findViewById(R.id.rvSearchlessons)

        // get search
        val search = intent.getBooleanExtra("search", false)
        if (search){
            val editTextSearch = findViewById<EditText>(R.id.editTextSearch)
            editTextSearch.requestFocus()
        }
        val modulItems = intent.getParcelableArrayListExtra<LmsDto>("data")
        listModul = modulItems?.map { it.toModulObject() }

        // starActin dan set data
        setDataModul(listModul)
        startAction()
    }

    fun LmsDto.toModulObject(): ModulItem {
        return ModulItem(
            cover = this.cover,
            createdAt = this.createdAt,
            view = this.view,
            section = this.section,
            id = this.id,
            title = this.title,
            totalsection = this.totalsection,
            progres = this.progres
        )
    }

    private fun startAction() {
//        binding.btnsearchLesson.setOnClickListener{
//            val text = binding.editTextSearch.text.toString()
//            //api
//            getDataSearch(text)
//        }
        binding.editTextSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)
            ) {
                val text = binding.editTextSearch.text.toString()
                if(text.length > 0){
                    getDataSearch(text)
                }else{
                    setDataModul(this.listModul)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getDataSearch(text: String) {
        val tag = "Get Data Search"
        ApiConfig.instanceRetrofit.searchLesson(
            "Bearer ${TOKEN}",
            text
        ).enqueue(object :Callback<searchResponse>{
            override fun onResponse(
                call: Call<searchResponse>,
                response: Response<searchResponse>
            ) {
               if (response.isSuccessful){
                   setDataSearch(response.body())
               }else{
                   when (response.code()) {
                       404 -> {
                           Log.e(tag, "Resource not found")
                           binding.textSearchNone.visibility = View.VISIBLE
                           binding.textSearchNone.text = "kata '${text}' tidak ditemukan"
                           // kosongkan recyclerviewSearch
                           recyclerviewSearch.adapter = null
                       }
                       else -> {
                           Log.e(tag, "Unexpected response code: ${response.code()}")
                       }
                   }
               }
            }
            override fun onFailure(call: Call<searchResponse>, t: Throwable) {
                Log.e(tag, "onFailure: ${t.message.toString()}")
            }

        })

    }

    private fun setDataSearch(body: searchResponse?) {
        val dataset = body?.data
        setDataModul(dataset)
    }

    private fun setDataModul(dataset: List<ModulItem>?) {
        binding.textSearchNone.visibility = View.GONE
        adapterLms = lessonAdapter(dataset!!, 2)
        recyclerviewSearch.adapter = adapterLms
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerviewSearch.layoutManager = layoutManager
    }


}