package com.dealjava.dealjava.ui.home.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.reindrairawan.organisasimahasiswa.data.DummyData
import com.reindrairawan.organisasimahasiswa.databinding.ItemSearchRecentlyViewedBinding

class RecentlyAdapter(
    private val context: Context
) : ListAdapter<DummyData, RecentlyAdapter.NewViewHolder>(DiffCallBack()) {

//    private var onItemClicked: OnItemClickNewDeal? = null
//
//    fun setOnItemClickedNewDeal(onItemNewDeal: OnItemClickNewDeal) {
//        this.onItemClicked = onItemNewDeal
//    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = NewViewHolder(
        ItemSearchRecentlyViewedBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun onBindViewHolder(holder: RecentlyAdapter.NewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NewViewHolder(private val binding: ItemSearchRecentlyViewedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DummyData) {
            binding.tvName.text = data.name
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<DummyData>() {
        override fun areItemsTheSame(
            oldItem: DummyData,
            newItem: DummyData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: DummyData,
            newItem: DummyData
        ): Boolean {
            return oldItem.name == newItem.name
        }
    }
//
//    interface OnItemClickNewDeal {
//        fun onItemClickedNewDeal()
//    }


}