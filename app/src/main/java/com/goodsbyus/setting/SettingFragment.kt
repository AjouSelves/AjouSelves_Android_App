package com.goodsbyus.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.goodsbyus.R
import com.goodsbyus.databinding.FragmentSettingBinding


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding?=null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSettingBinding.inflate(inflater, container, false)

        val view=binding.root
        return view
    }
}