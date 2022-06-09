package com.gonexwind.nexthotel.ui.explore

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.gonexwind.nexthotel.R
import com.gonexwind.nexthotel.core.data.local.entity.HotelEntity
import com.gonexwind.nexthotel.databinding.ItemVerticalBinding
import com.gonexwind.nexthotel.ui.home.HomeFragmentDirections

class ExploreAdapter(private val onBookmarkClick: (HotelEntity) -> Unit) :
    ListAdapter<HotelEntity, ExploreAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hotel = getItem(position)
        holder.bind(hotel)

        val bookmarkButton = holder.binding.bookmarkButton
        if (hotel.isBookmarked) {
            bookmarkButton.setImageDrawable(
                ContextCompat.getDrawable(bookmarkButton.context, R.drawable.ic_bookmark)
            )
        } else {
            bookmarkButton.setImageDrawable(
                ContextCompat.getDrawable(bookmarkButton.context, R.drawable.ic_bookmark_border)
            )
        }
        bookmarkButton.setOnClickListener { onBookmarkClick(hotel) }
    }

    class MyViewHolder(val binding: ItemVerticalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hotel: HotelEntity) {
            val (_, name, city, imageUrl, rate, description, priceRange) = hotel
            binding.apply {
                imageView.load(imageUrl)
                nameTextView.text = name
                cityTextView.text = city
                rateTextView.text = rate
                descTextView.text = description
                priceTextView.text = priceRange

                itemView.setOnClickListener {
                    val toDetail =
                        HomeFragmentDirections.actionNavigationHomeToDetailFragment(hotel)
                    it.findNavController().navigate(toDetail)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<HotelEntity> =
            object : DiffUtil.ItemCallback<HotelEntity>() {
                override fun areItemsTheSame(oldUser: HotelEntity, newUser: HotelEntity): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: HotelEntity,
                    newUser: HotelEntity
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }
}