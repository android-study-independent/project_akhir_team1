package com.example.tanify.ui.bottomNav.forum

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.forum.ForumItemsResponse
import com.example.tanify.databinding.FragmentForumBinding
import com.example.tanify.ui.bottomNav.forum.addDiscuss.AddDiscussActivity
import com.example.tanify.ui.bottomNav.forum.detailDiscuss.DetailDiscussActivity
import com.example.tanify.ui.bottomNav.forum.items.ItemFragmentForumAdapter
import com.google.android.material.snackbar.Snackbar
import com.example.tanify.R
import com.example.tanify.data.data.LikeForumData
import com.example.tanify.data.response.forum.LikeForumResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private lateinit var forumAdapter: ItemFragmentForumAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private var isUpdate: Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "ForumFragment"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val forumViewModel =
            ViewModelProvider(this).get(ForumViewModel::class.java)

        _binding = FragmentForumBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("Forum:", "onCreateForum")

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAction()
        setRecyclerView()
        getForum(true)
    }

    private fun setAction() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), AddDiscussActivity::class.java)
            startActivity(intent)
        }
        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //kosong
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //kosong
            }

            override fun afterTextChanged(p0: Editable?) {
                val searchForum = p0.toString()
                if (searchForum.isNotEmpty()) {
                    getSearchForum(searchForum, true)
                } else {
                    Log.e(TAG, "KOSONG")
                }
            }

        })
        binding.btnSearch.setOnClickListener {
            binding.linearSearchlms.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getForum(true)
        binding.linearSearchlms.visibility = View.GONE
    }

    private fun setRecyclerView(){
        forumAdapter = ItemFragmentForumAdapter(requireContext(), emptyList(),
            {},
            { forum ->
                val id = forum.id.toString()
                when(forum.likes) {
                    true -> putLikeForum(id, false)
                    else -> putLikeForum(id, true)
                }
            },
            { forum ->
                val id = forum.id
                val intent = Intent(requireContext(), DetailDiscussActivity::class.java)
                intent.putExtra("ID_FORUM", id)
                startActivity(intent)
            }
        )
        binding.rvForum.layoutManager = LinearLayoutManager(requireContext())
        binding.rvForum.adapter = forumAdapter
    }

    private fun getForum(showShimmer: Boolean){
        showLoading(true, showShimmer)
        ApiConfig.instanceRetrofit.getForum(
            "Bearer " + TOKEN
        ).enqueue(object : Callback<ForumItemsResponse>{
            override fun onResponse(
                call: Call<ForumItemsResponse>,
                response: Response<ForumItemsResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(false, showShimmer)
                    val currentForum = response.body()
                    if (currentForum?.data != null) {
                        forumAdapter.updateDataForumBeranda(currentForum.data.reversed())
                        Log.d(TAG, "SUKSES")
                    } else {
                        Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showLoading(false, showShimmer)
                }
            }

            override fun onFailure(call: Call<ForumItemsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
                showLoading(false, showShimmer)
            }
        })
    }

    private fun putLikeForum(id: String, likeStatus: Boolean) {
        val like = LikeForumData(likeStatus)
        ApiConfig.instanceRetrofit.postLikeForum(
            "Bearer " + TOKEN,
            id,
            like
        ).enqueue(object : Callback<LikeForumResponse>{
            override fun onResponse(
                call: Call<LikeForumResponse>,
                response: Response<LikeForumResponse>
            ) {
                if (response.isSuccessful) {
                    val likeForum = response.body()
                    getForum(false)
                    Log.d("Like", "Like : $likeForum")
                } else {
                    Log.e(TAG, "onFailure: ${response.code()}")
                    showSnackbar("gagal!!")
                }
            }

            override fun onFailure(call: Call<LikeForumResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                Log.e(TAG, "GAGAL PUT")
            }
        })
    }

    private fun getSearchForum(query: String, showShimmer: Boolean){
        showLoading(true, showShimmer)
        ApiConfig.instanceRetrofit.getSearchForum(
            "Bearer " + TOKEN,
            query
        ).enqueue(object : Callback<ForumItemsResponse>{
            override fun onResponse(
                call: Call<ForumItemsResponse>,
                response: Response<ForumItemsResponse>
            ) {
                val findForum = response.body()
                if (response.isSuccessful) {
                    showLoading(false, showShimmer)
                    if (findForum != null) {
                        forumAdapter.updateDataForumBeranda(findForum.data)
                    } else {
                        showSnackbar("Forum tidak ditemukan")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    showLoading(false, showShimmer)
                }
            }

            override fun onFailure(call: Call<ForumItemsResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure: ${t.message}")
                showLoading(false, showShimmer)
            }
        })
    }

    private fun showSnackbar(message: String) {
        val rootView: View = requireActivity().findViewById(android.R.id.content)
        val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun showLoading(isLoading: Boolean, showShimmer: Boolean){
        if (showShimmer) {
            if (isLoading) {
                binding.rvForum.visibility = View.GONE
                binding.shimmerView.visibility = View.VISIBLE
            } else {
                binding.rvForum.visibility = View.VISIBLE
                binding.shimmerView.visibility = View.GONE
            }
        } else {
            binding.shimmerView.isGone = true
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}