package com.anyline.ui

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anyline.R
import com.anyline.core.base.BaseActivity
import com.anyline.databinding.ActivityMainBinding
import com.anyline.di.DaggerAppComponent
import com.anyline.repository.GithubRepository
import com.anyline.ui.viewModel.MainViewModel
import com.anyline.ui.viewModel.MainViewModelImpl
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    @Inject
    lateinit var repository: GithubRepository

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