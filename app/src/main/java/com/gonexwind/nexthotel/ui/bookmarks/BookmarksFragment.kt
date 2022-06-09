package com.gonexwind.nexthotel.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gonexwind.nexthotel.core.ui.ViewModelFactory
import com.gonexwind.nexthotel.databinding.FragmentBookmarksBinding
import com.gonexwind.nexthotel.ui.home.HotelVerticalAdapter

class BookmarksFragment : Fragment() {

    private var _binding: FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: BookmarkViewModel by viewModels { factory }

        val hotelAdapter = HotelVerticalAdapter {
            if (it.isBookmarked) viewModel.deleteHotel(it) else viewModel.saveHotel(it)
        }

        viewModel.getBookmarkedHotel().observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            hotelAdapter.submitList(it)
        }

        binding.verticalRecyclerView.adapter = hotelAdapter
    }
}