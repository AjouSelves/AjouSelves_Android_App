package com.goodsbyus.ui.funding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FundingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is funding Fragment"
    }
    val text: LiveData<String> = _text
}