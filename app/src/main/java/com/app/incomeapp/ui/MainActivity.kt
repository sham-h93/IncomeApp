package com.app.incomeapp.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    var fragmentBackHandler:FragmentBackHandler? = null
    private lateinit var toast:Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initToast()

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

    private fun initToast() {
        val inflater = layoutInflater
        val layout = inflater.inflate(
            R.layout.toast,
            findViewById(R.id.root)
        )
        toast = Toast(this)
        toast.setGravity(Gravity.BOTTOM, 0, 100)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
    }

    fun setBackHandler(fragmentBackHandler: FragmentBackHandler){
        this.fragmentBackHandler=fragmentBackHandler
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp()
    }



    override fun onBackPressed() {
        fragmentBackHandler?.onBack(onFirstBackClick = toast::show){
            toast.cancel()
            super.onBackPressed()
        } ?:super.onBackPressed()
    }

}

interface FragmentBackHandler{
    fun onBack(onFirstBackClick:()->Unit, activityBackAction:()->Unit)
}
