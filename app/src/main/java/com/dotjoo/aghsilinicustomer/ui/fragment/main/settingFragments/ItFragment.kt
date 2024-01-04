package com.dotjoo.aghsilinicustomer.ui.fragment.main.settingFragments

 import android.text.TextUtils
 import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
 import com.dotjoo.aghsilinicustomer.data.PrefsHelper
 import com.dotjoo.aghsilinicustomer.data.remote.response.Message
 import com.dotjoo.aghsilinicustomer.databinding.FragmentItBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
 import com.dotjoo.aghsilinicustomer.ui.adapter.MessagesAdapter
 import com.dotjoo.aghsilinicustomer.util.Constants.EN
 import com.dotjoo.aghsilinicustomer.util.ext.hideKeyboard
import com.dotjoo.aghsilinicustomer.util.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItFragment : BaseFragment<FragmentItBinding>() {
    private val adapter by lazy { MessagesAdapter() }
    private var messages = mutableListOf<Message>()

    val mViewModel: SettingViewModel by viewModels()
    override fun onFragmentReady() {
        onClick()
        mViewModel.apply {
            getMessages()
            observe(viewState) {
                handleViewState(it)
            }

        }
        binding.swiperefreshHome.setOnRefreshListener {

          mViewModel.  getMessages()

            if (binding.swiperefreshHome != null) binding.swiperefreshHome.isRefreshing = false
        }

    }

    fun handleViewState(action: SettingAction) {
        when (action) {
            is SettingAction.ShowLoading -> {
                showProgress(action.show)
                if (action.show) {
                    hideKeyboard()
                }
            }


            is SettingAction.ShowFailureMsg -> action.message?.let {
                if (it.contains("401") == true) {
                    findNavController().navigate(R.id.loginFirstBotomSheetFragment)

                }else if (it.contains("aghsilini.com") == true) {
                    showToast(resources.getString(R.string.connection_error))
                } else {
                    showToast(action.message)
                    showProgress(false)
                }
            }

            is SettingAction.ShowAllMessage -> {
                action.data?.messages?.toMutableList()?.let {
                    messages =it
                    adapter.messages = messages
                    binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
                }
   }

            else -> {

            }
        }
    }
    lateinit var parent: MainActivity
    private fun onClick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        binding.messagesRv.adapter = adapter
        if(PrefsHelper.getLanguage()==EN)binding.sendImg.rotation=90f
        else binding.sendImg.rotation=270f

            binding.cardClose.setOnClickListener {
                findNavController().navigateUp()
            }

    binding . cardSend.setOnClickListener {
        if (!TextUtils.isEmpty(binding.etMessage.text)) {
            mViewModel.sendMessages(binding.etMessage.text.toString())
            adapter.addMessage(
                Message(

                    binding.etMessage.text.toString(),
                    "send"))


                        binding.etMessage.setText("")
            binding.messagesRv.smoothScrollToPosition(adapter.messages.size - 1)
        }
    }}

}