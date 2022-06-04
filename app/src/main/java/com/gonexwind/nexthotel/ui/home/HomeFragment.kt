package com.gonexwind.nexthotel.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gonexwind.nexthotel.adapter.HotelHorizontalAdapter
import com.gonexwind.nexthotel.adapter.HotelVerticalAdapter
import com.gonexwind.nexthotel.api.ApiConfig
import com.gonexwind.nexthotel.databinding.FragmentHomeBinding
import com.gonexwind.nexthotel.model.Hotel
import com.gonexwind.nexthotel.model.HotelsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
                    val horizontalAdapter = HotelHorizontalAdapter(responseBody.data)
                    binding.apply {
                        verticalRecyclerView.adapter = verticalAdapter
                        horizontalRecyclerView.adapter = horizontalAdapter
                    }

                    verticalAdapter.setOnItemClickCallback(object :
                        HotelVerticalAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Hotel) {
                            val toDetail = HomeFragmentDirections.actionNavigationHomeToDetailFragment(data)
                            view?.findNavController()?.navigate(toDetail)
                            Toast.makeText(requireActivity(), "HARUSNYA BISA", Toast.LENGTH_SHORT).show()
                        }
                    })

                    horizontalAdapter.setOnItemClickCallback(object :
                        HotelHorizontalAdapter.OnItemClickCallback {
                        override fun onItemClicked(data: Hotel) {
                            val toDetail = HomeFragmentDirections.actionNavigationHomeToDetailFragment(data)
                            view?.findNavController()?.navigate(toDetail)
                        }
                    })

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
        private const val TAG = "HomeFragment"
    }
}