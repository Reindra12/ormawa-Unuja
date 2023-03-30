package com.reindrairawan.organisasimahasiswa.presentation.dashboard.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reindrairawan.organisasimahasiswa.BuildConfig
import com.reindrairawan.organisasimahasiswa.databinding.ItemSearchKegiatanBinding
import com.reindrairawan.organisasimahasiswa.domain.kegiatan.entity.KegiatanEntity
import java.util.*

class GetKegiatanAdapter(
    private val context: Context,
    private val kegiatan: MutableList<KegiatanEntity>

) : RecyclerView.Adapter<GetKegiatanAdapter.ViewHolder>(), Filterable {

    private var onTapListener: OnItemTap? = null
    var filterKegiatan: MutableList<KegiatanEntity>

    init {
        filterKegiatan = kegiatan
    }

    interface OnItemTap {
        fun onTap(kegiatans: KegiatanEntity)
    }

    fun setItemTapListener(l: OnItemTap) {
        onTapListener = l
    }


    fun setData(mKegiatan: List<KegiatanEntity>?) {
        if (mKegiatan == null) return
        kegiatan.clear()
        kegiatan.addAll(mKegiatan)
        filterKegiatan = kegiatan
        notifyDataSetChanged()
    }

    fun updateList(mKegiatan: List<KegiatanEntity>) {
        kegiatan.clear()
        kegiatan.addAll(mKegiatan)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val itemBinding: ItemSearchKegiatanBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(kegiatans: KegiatanEntity) {
            itemBinding.namaKegiatanTv.text = kegiatans.nama_kegiatan
            itemBinding.diskripsiKegiatanTv.text = kegiatans.diskripsi_kegiatan
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_ASSETS + "kegiatan/" + kegiatans.gambar_kegiatan)
                .into(itemBinding.kegiatanImageview)
            itemBinding.jamKegiatanTv.text = kegiatans.jam_kegiatan
            itemBinding.tglKegiatanTv.text = kegiatans.tgl_kegiatan
            itemBinding.tempatKegiatanTv.text = kegiatans.tempat
            itemBinding.hariKegiatanTv.text = kegiatans.hari

            itemBinding.root.setOnClickListener {
                onTapListener?.onTap(kegiatans)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ItemSearchKegiatanBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(filterKegiatan[position])

    override fun getItemCount() = filterKegiatan.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    filterKegiatan = kegiatan
                } else {
                    val filteredList = ArrayList<KegiatanEntity>()
                    kegiatan
                        .filter {
                            (it.nama_kegiatan.lowercase().contains(constraint!!))

                        }
                        .forEach { filteredList.add(it) }
                    filterKegiatan = filteredList

                }
                return FilterResults().apply { values = filterKegiatan }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                filterKegiatan = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<KegiatanEntity>
                notifyDataSetChanged()
            }
        }
    }
}