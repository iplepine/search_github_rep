package com.zs.test.searchgitrepo.ui.main.model

import androidx.recyclerview.widget.RecyclerView
import com.zs.test.searchgitrepo.databinding.ItemRepositoryBinding

class RepositoryViewHolder(private val binding: ItemRepositoryBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: RepositoryItemViewModel) {
        binding.item = item
    }
}