package com.gonexwind.nexthotel.ui.search

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gonexwind.nexthotel.core.data.Result
import com.gonexwind.nexthotel.core.ui.ViewModelFactory
import com.gonexwind.nexthotel.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.actionBar?.hide()

        searchView()
    }

    private fun searchView() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: SearchViewModel by viewModels { factory }

        val searchAdapter = SearchAdapter {
            if (it.isBookmarked) viewModel.deleteHotel(it) else viewModel.saveHotel(it)
        }

        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                viewModel.searchHotel(q ?: "").observe(viewLifecycleOwner) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                val hotelData = it.data
                                searchAdapter.submitList(hotelData)
                                binding.searchRecyclerView.adapter = searchAdapter
                            }
                            is Result.Error -> showLoading(false)
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })
    }


    private fun showLoading(isLoading: Boolean) {
        when {
            isLoading -> binding.progressBar.visibility = View.VISIBLE
            else -> binding.progressBar.visibility = View.GONE
        }
    }
}