package com.anyline.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.anyline.R
import com.anyline.core.base.BaseFragment
import com.anyline.databinding.FragmentUserDetailsBinding
import com.anyline.ui.viewModel.MainViewModel


/**
 * A simple [BaseFragment] subclass.
 * create an instance of this fragment.
 */
class UserDetailsFragment : BaseFragment<FragmentUserDetailsBinding>() {

    private var userName: String = ""

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userName = arguments?.getString(MainFragment.userKey, "") ?: ""
        viewModel.getNetworkStatus().observe(this, {
            handleNetworkStatus(it)
        })
        viewModel.getUser().observe(this, {
            if (it != null) {
                dataBinding.user = it
            }
        })
    }

    override fun getResourceLayoutId(): Int = R.layout.fragment_user_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchUser(userName)
    }

    override fun showProgress(tag: String) {
        super.showProgress(tag)
        dataBinding.loading = true
    }

    override fun hideProgress(tag: String) {
        super.hideProgress(tag)
        dataBinding.loading = false
    }

}