package com.dotjoo.aghsilinicustomer.ui.activity

import android.content.Intent
 import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dotjoo.aghsilinicustomer.R
 import com.dotjoo.aghsilinicustomer.base.BaseActivity
 import com.dotjoo.aghsilinicustomer.databinding.ActivityAuthBinding
import com.dotjoo.aghsilinicustomer.util.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavController()
    }
    private fun setupNavController() {
        binding.progress =  baseShowProgress

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        try {
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.auth_nav)
            val extras = intent.extras
          if (extras != null) {
                val value = extras.getString(Constants.Start)

                if (value == Constants.login) {
                    graph.setStartDestination(R.id.loginFragment)

                } else if (value == Constants.SIGNUP) {
                    graph.setStartDestination(R.id.registerFragment)
                }
                else {
                    graph.setStartDestination(R.id.splashFragment)
                 }

                val navController = navHostFragment.navController
                navController.setGraph(graph, intent.extras)
        }
        }
        catch (e:Exception){

        }



    }

}