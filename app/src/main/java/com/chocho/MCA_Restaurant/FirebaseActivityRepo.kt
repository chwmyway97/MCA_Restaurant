package com.chocho.MCA_Restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseActivityRepo {

    private val database = Firebase.database
    private val tableDatabase = database.getReference("table")
    private val mutableData = MutableLiveData<MutableList<DataClassMeat>>()
    private val listData: MutableList<DataClassMeat> = mutableListOf()
    fun getData(): LiveData<MutableList<DataClassMeat>> {

        tableDatabase.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(tableSnapshot: DataSnapshot) {

                if (tableSnapshot.exists()) {

                    listData.clear()

                    for (tableMeatValue in tableSnapshot.children) {

                        val getData = tableMeatValue.getValue(DataClassMeat::class.java)

                        listData.add(getData!!)

                        mutableData.value = listData

                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {}

        })

        return mutableData

    }
}