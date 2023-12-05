package com.example.tanify.ui.bottomNav.forum

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.forum.ForumItemsResponse
import com.example.tanify.databinding.FragmentForumBinding
import com.example.tanify.ui.bottomNav.forum.detailDiscuss.DetailDiscussActivity
import com.example.tanify.ui.bottomNav.forum.items.ItemFragmentForumAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var forumAdapter: ItemFragmentForumAdapter
    private lateinit var sharedPreferences: SharedPreferences

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

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        TOKEN = sharedPreferences.getString("token", "").toString()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        getForum()
    }

    private fun setRecyclerView(){
        forumAdapter = ItemFragmentForumAdapter(requireContext(), emptyList(),
            {},
            {},
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

    private fun getForum(){
        ApiConfig.instanceRetrofit.getForum(
            "Bearer " + TOKEN
        ).enqueue(object : Callback<ForumItemsResponse>{
            override fun onResponse(
                call: Call<ForumItemsResponse>,
                response: Response<ForumItemsResponse>
            ) {
                if (response.isSuccessful) {
                    val currentForum = response.body()
                    if (currentForum?.data != null) {
                        forumAdapter.updateDataForumBeranda(currentForum.data)
                    } else {
                        Toast.makeText(requireContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ForumItemsResponse>, t: Throwable) {
                Log.e(TAG, "onFailure (OF): ${t.message.toString()}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}