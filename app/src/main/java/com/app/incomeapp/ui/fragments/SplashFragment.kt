package com.app.incomeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.app.incomeapp.R
import com.app.incomeapp.databinding.LayoutFragmentSplashBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    lateinit var binding: LayoutFragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.layout_fragment_splash,
            container, false
        )

        // (activity as AppCompatActivity?)!!.getSupportActionBar()!!.hide()

        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }

        return binding.root
    }
}