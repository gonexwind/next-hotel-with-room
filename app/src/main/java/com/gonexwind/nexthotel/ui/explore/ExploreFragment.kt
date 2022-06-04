package com.gonexwind.nexthotel.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gonexwind.nexthotel.adapter.HotelVerticalAdapter
import com.gonexwind.nexthotel.api.ApiConfig
import com.gonexwind.nexthotel.databinding.FragmentExploreBinding
import com.gonexwind.nexthotel.model.HotelsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHotels()
    }

    private fun getHotels() {
        showLoading(true)
        val client = ApiConfig.getApiService().getListHotel()
        client.enqueue(object : Callback<HotelsResponse> {
            override fun onResponse(
                call: Call<HotelsResponse>,
                response: Response<HotelsResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (responseBody != null) {
                    val verticalAdapter = HotelVerticalAdapter(responseBody.data)
                    binding.verticalRecyclerView.adapter = verticalAdapter
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HotelsResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading)
            binding.progressBar.visibility = View.VISIBLE
        else
            binding.progressBar.visibility = View.GONE
    }

    companion object {
        private const val TAG = "ExploreFragment"
    }
}