package com.chocho.MCA_Restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirebaseViewModel : ViewModel() {

    private val repo = FirebaseActivityRepo()
    private val mutableData = MutableLiveData<MutableList<DataClassMeat>>()

    fun fetchData(): LiveData<MutableList<DataClassMeat>> {

        repo.getData().observeForever {

            mutableData.value = it

        }

        return mutableData

    }

}