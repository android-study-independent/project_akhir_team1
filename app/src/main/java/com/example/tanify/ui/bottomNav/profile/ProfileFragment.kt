package com.example.tanify.ui.bottomNav.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tanify.databinding.FragmentProfileBinding
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangePasswordActivity
import com.example.tanify.ui.bottomNav.profile.editProfile.ChangeProfileActivity
import com.example.tanify.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        private const val TAG = "ProfileFragment"
        private const val PREF_NAME = "MyAppPreferences"
        private const val TOKEN = "token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        //val textView: TextView = binding.textNotifications
        profileViewModel.text.observe(viewLifecycleOwner) {
            //textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnLogout.setOnClickListener {
            val tokenBefore = sharedPreferences.getString(TOKEN, "")
            Log.d(TAG, "token sebelum : $tokenBefore")

            with(sharedPreferences.edit()) {
                remove(TOKEN)
                apply()

                val tokenAfter = sharedPreferences.getString(TOKEN, "")
                Log.d(TAG, "token setelah : $tokenAfter")
            }

            // Navigasi ke halaman login
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnUbahProfil.setOnClickListener{
            val intent = Intent(requireContext(), ChangeProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.btnUbahPw.setOnClickListener{
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}