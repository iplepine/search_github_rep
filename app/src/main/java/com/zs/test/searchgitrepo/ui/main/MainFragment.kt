package com.zs.test.searchgitrepo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zs.test.searchgitrepo.data.common.showToast
import com.zs.test.searchgitrepo.databinding.MainFragmentBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initViews()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.toastEvent.observe(viewLifecycleOwner, Observer {
            showToast(it)
        })
    }

    private fun initViews() {
        initQueryView()
        initRecyclerView()
        initProgress()
    }

    private fun initQueryView() {
        binding.queryInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.onClickSearch()
                true
            }
            false
        }
    }

    private fun initRecyclerView() {
        binding.resultView.apply {
            adapter = viewModel.initAdapter()
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    viewModel.onRecyclerViewScrolled(recyclerView)
                }
            })
        }
    }

    private fun initProgress() {
        viewModel.isProgress.observe(viewLifecycleOwner, Observer<Boolean> {
            binding.progress.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }
}