package com.reindrairawan.organisasimahasiswa.presentation.fuzzy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.data.fuzzy.FuzzyEntity
import com.reindrairawan.organisasimahasiswa.databinding.CustomCardviewBinding


class FuzzyAdapter(val c: Context, val fuzzyList: ArrayList<FuzzyEntity>) :
    RecyclerView.Adapter<FuzzyAdapter.ListViewHolder>() {

    var onItemClick: ((FuzzyEntity) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ListViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.custom_cardview, parent, false
        )
    )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = fuzzyList[position]
        holder.bind(data)
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        private val binding = CustomCardviewBinding.bind(itemView)
        fun bind(data: FuzzyEntity) {
            with(binding) {

                name.text = data.nama
                variabelNaikTextview.text = data.nama
                variableTurunTextview.text = data.nama
                naikTextview.text = data.nilaiTinggi + " Tertinggi"
                turunTextview.text = data.nilaiTurun + " Terendah"

            }
        }

        init {
//            mMenus = itemView.findViewById(R.id.menu_image)
//            binding.root.setOnClickListener {
//                onItemClick?.invoke(fuzzyList[adapterPosition])
//            }
            binding.menuImage.setOnClickListener {
                popUpMenu(it)
            }
        }

        private fun popUpMenu(v: View) {
            val position = fuzzyList[adapterPosition]
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.edit_menu -> {
                        onItemClick?.invoke(position)
                        true
                    }
                    R.id.delete_menu -> {
                        fuzzyList.removeAt(adapterPosition)
                        notifyDataSetChanged()
                        true
                    }
                    else -> true

                }

            }

            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)

        }
    }

    override fun getItemCount() = fuzzyList.size

}