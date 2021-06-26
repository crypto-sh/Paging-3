package com.anyline.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.anyline.R
import com.anyline.core.base.BaseFragment
import com.anyline.databinding.FragmentMainBinding

import com.anyline.ui.adapter.RvAdapterUsers
import com.anyline.ui.viewModel.MainViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * A simple [BaseFragment] subclass.
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {

    companion object {
        const val userKey = "userKey"
    }

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter: RvAdapterUsers by lazy {
        RvAdapterUsers { selected ->
            findNavController().navigate(R.id.userDetailsFragment, Bundle().apply {
                this.putString(userKey, selected.login)
            })
        }.also {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    dataBinding.loading = loadStates.mediator?.refresh is LoadState.Loading
                    dataBinding.error = loadStates.mediator?.refresh is LoadState.Error
                }
            }
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.getUsers().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding.recycler.adapter = adapter
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_main
}