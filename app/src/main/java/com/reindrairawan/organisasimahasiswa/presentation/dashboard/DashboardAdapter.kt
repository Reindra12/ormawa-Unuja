package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reindrairawan.organisasimahasiswa.databinding.ItemCategoryBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity

class DashboardAdapter(private val categories: MutableList<CategoriesEntity>) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
    private var onTapListener: DashboardAdapter.OnItemTap? = null

    interface OnItemTap {
        fun onTap(categories: CategoriesEntity)
    }

    fun setItemTapListener(l: OnItemTap) {
        onTapListener = l
    }


    fun setData(mCategories: List<CategoriesEntity>?) {
        if (mCategories == null) return
        categories.clear()
        categories.addAll(mCategories)
        notifyDataSetChanged()
    }

    fun updateList(mCategories: List<CategoriesEntity>) {
        categories.clear()
        categories.addAll(mCategories)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val itemBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(categories: CategoriesEntity) {
            itemBinding.categoriesNameTextview.text = categories.name
            Glide.with(itemView.context)
                .load("http://192.168.0.182:8000/"+categories.path)
                .into(itemBinding.categoriesImageview)

            itemBinding.root.setOnClickListener {
                onTapListener?.onTap(categories)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(categories[position])

    override fun getItemCount() = categories.size

}