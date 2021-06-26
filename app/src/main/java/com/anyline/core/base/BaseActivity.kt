package com.anyline.core.base


import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.OpenableColumns
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anyline.R
import com.anyline.dto.ErrorType
import com.anyline.dto.NetworkState
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<VM : BaseViewModel, E : ViewDataBinding> : AppCompatActivity() {

    var isMainActivity: Boolean = false

    lateinit var viewModel: VM

    lateinit var dataBinding: E

    abstract fun inject()

    abstract fun getResourceLayoutId(): Int

    abstract fun getViewModelClass(): Class<VM>

    abstract fun getFactory(): ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getResourceLayoutId())
        viewModel = ViewModelProvider(this, getFactory()).get(getViewModelClass())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (!isMainActivity)
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    open fun handleSearchView(menuItem: MenuItem, onTextChanged: (String) -> Unit) {
        val searchView = menuItem.actionView as SearchView?
        var timer: CountDownTimer? = null
        var previousText = ""
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = false
            override fun onQueryTextChange(newText: String): Boolean {
                timer?.cancel()
                timer = object : CountDownTimer(500, 500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        if (previousText != newText) {
                            previousText = newText
                            onTextChanged(newText)
                        }
                    }
                }.start()
                return true
            }
        })
    }

    open fun initToolbar(toolbar: Toolbar, title: String? = null) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title ?: ""
    }

    open fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    open fun showMessage(msg: String) {
        Snackbar.make(dataBinding.root, msg, Snackbar.LENGTH_LONG)
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .show()
    }

    open fun isTablet(): Boolean {
        val screen = resources.configuration.screenLayout
        return screen and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    open fun addFragment(@IdRes layoutId: Int, fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(layoutId, fragment)
            .hide(fragment)
            .commit()
    }

    open fun showProgress(tag: String) {}

    open fun hideProgress(tag: String) {}

    open fun showError(tag: String, error: String, type: ErrorType) {
        hideProgress(tag)
    }

    open fun getSelectedFileName(uri: Uri): String? {
        return contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        }
    }

    open fun handleFailureStatus(status: NetworkState, onShowMessage: (String) -> Unit) {
        hideProgress(status.tag)
        when (status.type) {
            ErrorType.Authorization -> {
                //logout user
            }
            ErrorType.HttpException -> {
                if (status.msg.isEmpty()) {
                    onShowMessage("${getString(status.type.resource)} -- ${status.code}")
                } else {
                    onShowMessage(status.msg)
                }
            }
            else -> {
                onShowMessage(
                    if (status.msg.isEmpty()) {
                        getString(status.type.resource) //+ if (status.code ?: 0 > 0) "with code : ${status.code}" else ""
                    } else {
                        status.msg
                    }
                )
            }
        }
    }
}