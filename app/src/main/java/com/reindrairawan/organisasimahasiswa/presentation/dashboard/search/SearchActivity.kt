package com.reindrairawan.organisasimahasiswa.presentation.dashboard.search

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dealjava.dealjava.ui.home.search.RecentlyAdapter
import com.google.android.material.chip.Chip
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.DummyData
import com.reindrairawan.organisasimahasiswa.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private val binding: ActivitySearchBinding by lazy {
        ActivitySearchBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var recentlyAdapter: RecentlyAdapter
    
    private val dummyData = mutableListOf(
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta"),
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta"),
        DummyData("Breakfast Buffet at Hotel Grandhika Iskandaryah Jakarta")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        window.statusBarColor = Color.TRANSPARENT;
        window.statusBarColor = ContextCompat.getColor(this, R.color.white);
        setContentView(binding.root)
        recentlyAdapter = RecentlyAdapter(this)
        binding.rvRecently.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = recentlyAdapter
        }
        recentlyAdapter.submitList(dummyData)

        binding.imgBack.setOnClickListener {
            finish()
        }
        binding.imgClearSearch.setOnClickListener {
            binding.edtSearch.text?.clear()
        }

        setChip()
    }

    private fun createTagChip(context: Context, chipName: String): Chip {
        return Chip(context).apply {
            text = chipName
            setChipBackgroundColorResource(R.color.color_grey_light)
            setTextColor(ContextCompat.getColor(context, R.color.relative))
        }

    }

    private fun setChip() {
        val array = arrayListOf("richeese factory", "mcdonals", "all you can eat", "homestay", "hotel")
        array.forEach {
            binding.chipHistory.addView(createTagChip(this, it))
        }
    }
}