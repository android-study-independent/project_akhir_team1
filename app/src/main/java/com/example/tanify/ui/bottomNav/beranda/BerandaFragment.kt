package com.example.tanify.ui.bottomNav.beranda

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanify.data.api.weather.ApiWeatherConfig
import com.example.tanify.data.response.CurrentWeatherResponse
import com.example.tanify.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.tanify.R
import com.example.tanify.data.data.ArtikelBerandaItemData
import com.example.tanify.data.data.FiturItemData
import com.example.tanify.helper.kelvinToCelcius
import com.example.tanify.ui.bottomNav.beranda.items.ItemBerandaArtikelAdapter
import com.example.tanify.ui.bottomNav.beranda.items.ItemFiturAdapter

class BerandaFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val fiturList = ArrayList<FiturItemData>()
    private val artikelList = ArrayList<ArtikelBerandaItemData>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "BerandaFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val brandaViewModel =
            ViewModelProvider(this).get(BrandaViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        brandaViewModel.text.observe(viewLifecycleOwner) {
//             textView.text = it
//        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFiturUtama.setHasFixedSize(true)
        binding.rvFiturUtama.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.rvArtikelBeranda.setHasFixedSize(true)
        binding.rvArtikelBeranda.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        addDataToList()
        val adapterRvFitur = ItemFiturAdapter(fiturList)
        binding.rvFiturUtama.adapter = adapterRvFitur

        val adapterRvArtikel = ItemBerandaArtikelAdapter(artikelList)
        binding.rvArtikelBeranda.adapter = adapterRvArtikel

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        getMyLocation()
    }

    //On destroy
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //Get user lat & lon
    private fun getMyLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val lat = it.latitude
                    val lon = it.longitude
                    Log.d(TAG, "lat = $lat\nlon = $lon")
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    //User permission request
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLocation()
                }

                else -> {
                    // No location access granted.
                }
            }
        }

    //User permission checker
    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addDataToList() {

        fiturList.add(FiturItemData(R.drawable.lms_icon, "LMS"))
        fiturList.add(FiturItemData(R.drawable.lms_icon, "LMS"))
        fiturList.add(FiturItemData(R.drawable.lms_icon, "LMS"))

        artikelList.add(ArtikelBerandaItemData(R.drawable.unsplash_poster, "Cara meningkatkan hasil pertanian\n" +
                "dengan metode ini..."))
        artikelList.add(ArtikelBerandaItemData(R.drawable.unsplash_poster, "Cara meningkatkan hasil pertanian\n" +
                "dengan metode ini..."))
        artikelList.add(ArtikelBerandaItemData(R.drawable.unsplash_poster, "Cara meningkatkan hasil pertanian\n" +
                "dengan metode ini..."))
    }
}