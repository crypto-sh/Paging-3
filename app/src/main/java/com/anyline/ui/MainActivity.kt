package com.anyline.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.*
import androidx.paging.LoadState
import com.anyline.R
import com.anyline.core.base.BaseActivity
import com.anyline.databinding.ActivityMainBinding
import com.anyline.di.DaggerAppComponent
import com.anyline.repository.GithubRepository
import com.anyline.ui.adapter.RvAdapterUsers
import com.anyline.ui.viewModel.MainViewModel
import com.anyline.ui.viewModel.MainViewModelImpl
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private val adapter: RvAdapterUsers by lazy {
        RvAdapterUsers().also {
            lifecycleScope.launchWhenCreated {
                adapter.loadStateFlow.collectLatest { loadStates ->
                    dataBinding.loading = loadStates.mediator?.refresh is LoadState.Loading
                    dataBinding.error = loadStates.mediator?.refresh is LoadState.Error
                }
            }
            lifecycleScope.launchWhenCreated {
                viewModel.getUsers().collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    @Inject
    lateinit var repository: GithubRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding.recycler.adapter = adapter
    }

    override fun inject() {
        DaggerAppComponent.builder().application(application).build().inject(this)
    }

    override fun getResourceLayoutId(): Int = R.layout.activity_main

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun getFactory(): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(this, null) {
        override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
            @Suppress("UNCHECKED_CAST")
            return MainViewModelImpl(repository, handle) as T
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                handleSearchView(item) { newText ->
                    viewModel.fetchUsers(newText)
                }
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}