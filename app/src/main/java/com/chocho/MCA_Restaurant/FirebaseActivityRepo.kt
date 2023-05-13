package com.chocho.MCA_Restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FirebaseActivityRepo {
    fun getData(): LiveData<MutableList<MeatDataClass>> {
        val mutableData = MutableLiveData<MutableList<MeatDataClass>>()
        val database = Firebase.database
        val myRef = database.getReference("table")

        myRef.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<MeatDataClass> = mutableListOf<MeatDataClass>()

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    listData.clear()
                    for (meatSnapshot in snapshot.children){
                        val getData = meatSnapshot.getValue(MeatDataClass::class.java)
                        listData.add(getData!!)

                        mutableData.value = listData
                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return mutableData
    }
}