package com.chocho.MCA_Restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SubViewModel : ViewModel() {
    private val repo = SubActivityRepo()
    fun fetchData(): LiveData<MutableList<Meat>>{
        val mutableData = MutableLiveData<MutableList<Meat>>()
        repo.getData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }

}