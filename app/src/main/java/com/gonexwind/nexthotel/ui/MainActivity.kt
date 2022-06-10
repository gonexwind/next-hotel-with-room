package com.gonexwind.nexthotel.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gonexwind.nexthotel.R
import com.gonexwind.nexthotel.core.data.Result
import com.gonexwind.nexthotel.core.ui.ViewModelFactory
import com.gonexwind.nexthotel.databinding.ActivityMainBinding
import com.gonexwind.nexthotel.ui.home.BestPickAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment -> {
                    navView.visibility = View.GONE
                    binding.topBar.visibility = View.GONE
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    binding.topBar.visibility = View.VISIBLE
                }
            }
        }

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        val adapter = BestPickAdapter {
            if (it.isBookmarked) viewModel.deleteHotel(it) else viewModel.saveHotel(it)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.searchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        if (searchView.isActivated) {
            binding.searchResult.visibility = View.VISIBLE
        } else {
            binding.searchResult.visibility = View.GONE
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?): Boolean {
                viewModel.searchHotel(q ?: "").observe(this@MainActivity) {
                    if (it != null) {
                        when (it) {
                            is Result.Loading -> showSearchLoading(true)
                            is Result.Success -> {
                                showSearchLoading(false)
                                val hotelData = it.data
                                adapter.submitList(hotelData)
                                binding.searchRecyclerView.adapter = adapter
                            }
                            is Result.Error -> {
                                showSearchLoading(false)
                            }
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })
    }

    private fun showSearchLoading(isLoading: Boolean) {
        when {
            isLoading -> {
                binding.searchProgressBar.visibility = View.VISIBLE
                binding.searchRecyclerView.visibility = View.GONE
            }
            else -> {
                binding.searchProgressBar.visibility = View.GONE
                binding.searchRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}