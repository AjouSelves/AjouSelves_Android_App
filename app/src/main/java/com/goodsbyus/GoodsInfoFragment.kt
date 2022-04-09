package com.goodsbyus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.goodsbyus.databinding.FragmentGoodsInfoBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [GoodsInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GoodsInfoFragment : Fragment() {
    private var _binding: FragmentGoodsInfoBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGoodsInfoBinding.inflate(inflater, container, false)

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            val id_str = bundle.getString("id")
            val url_str = bundle.getString("url")
            val title_str = bundle.getString("title")

            binding.idView.text=id_str
            Glide.with(this)
                .load(url_str)
                .into(binding.imageView)
            binding.titleView.text=title_str
        }

        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}