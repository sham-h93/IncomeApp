package com.app.incomeapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.app.incomeapp.R
import com.app.incomeapp.databinding.ActivityMainBinding
import com.app.incomeapp.ui.fragments.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    override fun onBackPressed() {
        when(currentFragId) {
            R.id.mainFragment -> {
                super.onBackPressed()
            }
        }

    }

//    override fun onBackPressed() {
//        supportFragmentManager.findFragmentById(R.id.fragment_container_view)?.childFragmentManager?.let {
//            if(it.fragments.isEmpty().not())
//            {
//                 it.fragments.forEach { fragment ->
//                     if(fragment is MainFragment){
//                        if(fragment.onBackPress()){
//                            println("Aaa")
//                        }
//                         else println("bbb")
//                     }
//                 }
//            }
//            else println("B")
//        }
//    }

}

