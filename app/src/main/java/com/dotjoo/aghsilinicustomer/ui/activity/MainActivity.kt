package com.dotjoo.aghsilinicustomer.ui.activity

 import android.os.Bundle
 import android.view.WindowManager
 import androidx.activity.viewModels
 import androidx.core.view.isVisible
 import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dotjoo.aghsilinicustomer.R
import com.dotjoo.aghsilinicustomer.base.BaseActivity
import com.dotjoo.aghsilinicustomer.data.PrefsHelper
import com.dotjoo.aghsilinicustomer.databinding.ActivityMainBinding
 import com.dotjoo.aghsilinicustomer.ui.fragment.main.home.HomeViewModel
 import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    val mViewModel : HomeViewModel by viewModels ()

    companion object {
        const val MAIN_SCREEN_ACTION = "MAIN_SCREEN_ACTION"
    }
    var lat: Double? = null
    var long: Double? = null
    var address: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.progress =  baseShowProgress
        setupNavController()
        mViewModel.updateToken()
    }

    fun showBottomNav(isVisible: Boolean) {
        binding.navView.isVisible = isVisible
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        var navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)
        binding.navView.itemIconTintList = null
        var menu = binding.navView.menu
        if (PrefsHelper.getToken().isNullOrEmpty()) {
    menu.findItem(R.id.accountFragment) .isVisible = false
    menu.findItem(R.id.basketkFragment) .isVisible = false


        }
        else{
            menu.findItem(R.id.accountFragment) .isVisible = true
            menu.findItem(R.id.basketkFragment) .isVisible = true
        }


        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.orderFragment -> {
                    if (PrefsHelper.getToken().isNullOrEmpty()) {

                        navController.navigate(R.id.loginFirstBotomSheetFragment)
                    } else {
                        navController.navigate(
                            R.id.orderFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                        )

                    }
                    true
                }

                R.id.basketkFragment -> {


                    if (PrefsHelper.getToken().isNullOrEmpty()) {

                        navController.navigate(R.id.loginFirstBotomSheetFragment)
                    } else {
                        navController.navigate(
                            R.id.basketkFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                        )
                    }
                    true
                }

                R.id.accountFragment -> {
                    if (PrefsHelper.getToken().isNullOrEmpty()) {

                        navController.navigate(R.id.loginFirstBotomSheetFragment)
                    } else {
                        navController.navigate(
                            R.id.accountFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                        )
                    }
                    true
                }

                R.id.moreFragment -> {
                    navController.navigate(
                        R.id.moreFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.homeFragment, false).build()
                    )

                    true
                }

                else -> {
                    true
                }
            }
        }
    }

}