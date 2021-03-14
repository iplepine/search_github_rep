package com.zs.test.searchgitrepo.ui.main.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zs.test.searchgitrepo.databinding.ItemRepositoryBinding

class RepositoryAdapter(val data: ArrayList<RepositoryItemViewModel>) : RecyclerView.Adapter<RepositoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        if (position < data.size) {
            holder.bind(data[position])
        }
    }
}