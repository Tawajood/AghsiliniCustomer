package com.dotjoo.aghsilinicustomer.ui.fragment.main.checkout

  import androidx.core.os.bundleOf
  import androidx.fragment.app.activityViewModels
  import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseFragment
import com.dotjoo.aghsilinicustomer.databinding.FragmentSuccessfulOrderBinding
import com.dotjoo.aghsilinicustomer.ui.activity.MainActivity
import com.dotjoo.aghsilinicustomer.ui.fragment.main.basket.CreateOrderViewModel
 import com.dotjoo.aghsilinicustomer.util.Constants
 import com.dotjoo.aghsilinicustomer.util.ext.showActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SuccessfulOrderFragment : BaseFragment<FragmentSuccessfulOrderBinding>() {
val mViewModel : CreateOrderViewModel by activityViewModels()
    lateinit var parent: MainActivity
    private fun onclick() {
        parent = requireActivity() as MainActivity
        parent.showBottomNav(false)
        mViewModel.order_id?.let {
            binding.tvId.setText(resources.getString(R.string.orderid_1234)+  mViewModel.order_id)
        }
  binding.btnContinue.setOnClickListener {
      findNavController().navigate(
          R.id.orderInfoFragment,
         bundleOf(Pair(Constants.ORDER_ID, mViewModel.order_id)),
          NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
      )
  }
      binding.btnHome.setOnClickListener {
   showActivity(MainActivity::class.java, clearAllStack = true)

      }

    }




    override fun onFragmentReady() {
        onclick()
        binding.animationView.playAnimation()

     }

}