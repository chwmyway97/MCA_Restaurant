package com.chocho.MCA_Restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirebaseViewModel : ViewModel() {
    private val repo = FirebaseActivityRepo()
    fun fetchData(): LiveData<MutableList<MeatDataClass>>{
        val mutableData = MutableLiveData<MutableList<MeatDataClass>>()
        repo.getData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }

}