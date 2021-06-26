package com.anyline.core.base

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.anyline.R
import com.anyline.dto.NetworkState
import com.anyline.dto.Status
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<E : ViewDataBinding> : Fragment() {

    lateinit var dataBinding: E

    private val permissionRequest: Int = 12000

    inline fun <reified VM : BaseViewModel> initialize(): Lazy<VM> {
        return object : Lazy<VM> {
            private var cached: VM? = null
            override val value: VM
                get() {
                    return cached ?: ViewModelProvider(requireActivity()).get(VM::class.java).also {
                        cached = it
                    }
                }

            override fun isInitialized(): Boolean = cached != null
        }
    }

    abstract fun getResourceLayoutId(): Int

    open fun inject() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    open fun handleNetworkStatus(network: NetworkState? = null) {
        if (network != null) {
            when (network.status) {
                Status.RUNNING -> showProgress(network.tag)
                Status.SUCCESS -> hideProgress(network.tag)
                else -> {
                    hideProgress(network.tag)
                    showMessage(network.msg)
                }
            }
        } else {
            hideProgress("")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getResourceLayoutId(),
            container,
            false
        )
        return dataBinding.root
    }

    fun hideKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboardByTouch(view: View) {
        view.setOnClickListener {
            hideKeyboard(view)
        }
    }

    fun showKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    fun isTablet(): Boolean {
        val screen = requireContext().resources.configuration.screenLayout
        return screen and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    open fun showMessage(msg: String) {
        if (isAdded) {
            Snackbar.make(dataBinding.root, msg, Snackbar.LENGTH_LONG)
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .show()
        }
    }

    open fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    open fun checkPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) == PackageManager.PERMISSION_DENIED
            )
                return false
        }
        return true
    }

    open fun showProgress(tag: String = "") {}
    open fun hideProgress(tag: String = "") {}
}