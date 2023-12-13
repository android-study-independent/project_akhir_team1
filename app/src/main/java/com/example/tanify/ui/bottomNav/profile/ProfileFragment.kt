package com.example.tanify.ui.bottomNav.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.tanify.R
import com.example.tanify.data.api.tanify.ApiConfig
import com.example.tanify.data.response.profile.UserProfilResponse
import com.example.tanify.databinding.FragmentProfileBinding
import com.example.tanify.helper.GetUserProfilCallback
import com.example.tanify.helper.getUserProfil
import com.example.tanify.ui.bottomNav.forum.ForumViewModel
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangePasswordActivity
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangeProfileActivity
import com.example.tanify.ui.login.LoginActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences
    private var dataprofil: UserProfilResponse? = null
    val requestCode = 123

    companion object {
        private const val TAG = "ProfileFragment"
        private const val PREF_NAME = "MyAppPreferences"
        private var TOKEN = "token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        Log.d("Profile:", "onCreate Profile")

        // sharedPreferences data
        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        //ambil data login
        TOKEN = sharedPreferences.getString("token", "").toString()
        Log.d("token", TOKEN)

        // ambil data profil
        getProfil(false)

        // Inisialisasi swipeRefreshLayout
        swipeRefreshLayout = binding.swipeRefreshLayout

        return root
    }


    private fun processDataProfil(profilData: UserProfilResponse?, isUpdate: Boolean = false) {
        Log.d(TAG, "set data profil ke ui")
        binding.profilName.text = profilData?.nama
        binding.profilEmail.text = profilData?.email

        val foto = profilData?.photo?.removePrefix("../")
        Glide.with(this)
//            .load("http://195.35.32.179:8001/" + foto)
            .load(profilData?.photo)
            .skipMemoryCache(isUpdate)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(R.drawable.icon_user)
            .error(R.drawable.icon_user)
            .into(binding.profilImg)
    }


    private fun getProfil(b: Boolean) {
        getUserProfil(TOKEN, object : GetUserProfilCallback{
            override fun onUserProfileReceived(userProfil: UserProfilResponse) {
                dataprofil = userProfil
                processDataProfil(dataprofil, b)
            }

            override fun onFailed(message: String) {
                // token infalid
                Log.e(TAG, "get Profile: ${message}")
            }

        } )
    }



    private fun saveProfilToSharedPreferences(profilData: UserProfilResponse?) {
        val gson = Gson()
        val profilDataString = gson.toJson(profilData)

        with(sharedPreferences.edit()) {
            putString("profil", profilDataString)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            val tokenBefore = sharedPreferences.getString(TOKEN, "")
            Log.d(TAG, "token sebelum : $tokenBefore")
            with(sharedPreferences.edit()) {
                remove("token")
                remove("profil")
                apply()
            }

            // Navigasi ke halaman login
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnUbahProfil.setOnClickListener {
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            startActivityForResult(intent, requestCode)


        }
        binding.btnUbahPw.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            Log.d("loading", "==== true ========================================================")
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            Log.d("loading", "==== false ========================================================")
            binding.progressCircular.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        showLoading(true)
        getProfil(true)
        Toast.makeText(requireContext(), "Refresh", Toast.LENGTH_SHORT).show()
        showLoading(false)
        swipeRefreshLayout.isRefreshing = false
    }

}