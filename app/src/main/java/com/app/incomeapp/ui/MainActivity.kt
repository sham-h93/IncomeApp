package com.app.incomeapp.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.app.incomeapp.R
import com.app.incomeapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var bindView: ActivityMainBinding
    lateinit var navController: NavController
    var currentFragId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = this.findNavController(R.id.fragment_container_view)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.splashFragment -> {
                    currentFragId = R.id.mainFragment
                }
                R.id.mainFragment -> {
                    supportActionBar?.apply {
                        show()
                        title = "مدیریت دخل و خرج"
                    }
                }
            }
        }
    }
}