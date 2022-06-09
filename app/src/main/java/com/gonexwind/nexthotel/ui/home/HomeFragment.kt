package com.gonexwind.nexthotel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gonexwind.nexthotel.core.data.Result
import com.gonexwind.nexthotel.core.ui.ViewModelFactory
import com.gonexwind.nexthotel.databinding.FragmentHomeBinding

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: HomeViewModel by viewModels { factory }

        val hotelVerticalAdapter = HotelVerticalAdapter {
            if (it.isBookmarked) viewModel.deleteHotel(it) else viewModel.saveHotel(it)
        }
        val hotelHorizontalAdapter = HotelHorizontalAdapter {
            if (it.isBookmarked) viewModel.deleteHotel(it) else viewModel.saveHotel(it)
        }

        viewModel.getListHotel().observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val hotelData = it.data
                        hotelVerticalAdapter.submitList(hotelData)
                        hotelHorizontalAdapter.submitList(hotelData)
                    }
                    is Result.Error -> {
                        showLoading(true)
                        Toast.makeText(
                            context,
                            "Please Check Your Internet",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.verticalRecyclerView.adapter = hotelVerticalAdapter
        binding.horizontalRecyclerView.adapter = hotelHorizontalAdapter
    }

    private fun showLoading(isLoading: Boolean) {
        when {
            isLoading -> binding.progressBar.visibility = View.VISIBLE
            else -> binding.progressBar.visibility = View.GONE
        }
    }
}