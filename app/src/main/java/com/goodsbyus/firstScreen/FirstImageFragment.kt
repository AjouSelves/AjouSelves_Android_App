package com.goodsbyus.firstScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodsbyus.R
import com.goodsbyus.databinding.FragmentFirstImageBinding

class FirstImageFragment : Fragment() {

    private var _binding: FragmentFirstImageBinding?=null
    private val binding get()=_binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentFirstImageBinding.inflate(inflater,container,false)

        val view=binding.root
        return view
    }
}