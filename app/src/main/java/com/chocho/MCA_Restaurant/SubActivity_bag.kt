package com.chocho.MCA_Restaurant


import android.app.ActivityManager
import android.content.Intent
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.BandPredicate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.AllPermission
import kotlin.coroutines.EmptyCoroutineContext
import com.google.firebase.database.snapshot.EmptyNode as Empty


class SubActivity_bag : AppCompatActivity() {

    //파이어베이스 변수
    private var myRef: DatabaseReference? = null

    //리사이클러뷰 변수
    private lateinit var adapter: SublistAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(SubViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bag)

        overridePendingTransition(R.anim.fade_in, R.anim.none) // 애니매이션

        //버튼 변수
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val payment = findViewById<ImageButton>(R.id.payment)
        val removeButton =findViewById<ImageButton>(R.id.removeButton)

        //intent 변수
        val intentMainActivity2 = Intent(this, MainActivity2::class.java)

        //뒤로가기 버튼
        backButton.setOnClickListener {
            intentMainActivity2.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intentMainActivity2)
        }

        adapter = SublistAdapter(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView2)
        //RecyclerView 같은 경우 메니져를 설정해줘야한다
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter



        observerData()

        val allValue: TextView = findViewById(R.id.allValue)
        val database = Firebase.database

        myRef = database.getReference("table")
        myRef?.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                var sum = 0
                val dec = DecimalFormat("###,###")
                if (p0.exists()) {

                    for (s in p0.children) {

                        sum += s.child("meatValue").getValue(Int::class.java)!!

                        allValue.text = "￦ " + dec.format(sum)
                    }

                    payment.setOnClickListener {

                        val intentWebView = Intent(this@SubActivity_bag, WebView::class.java)

                        startActivity(intentWebView)
                    }

                    removeButton.setOnClickListener{
                        myRef?.removeValue()

                        Toast.makeText(this@SubActivity_bag, "전체 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                        val intentMain = Intent(this@SubActivity_bag, MainActivity2::class.java)
                        startActivity(intentMain)
                    }

                } else {

                    sum = 0
                    allValue.text = "￦ " + dec.format(sum)
                    payment.setOnClickListener {
                        Toast.makeText(this@SubActivity_bag, "구매 목록이 없습니다.", Toast.LENGTH_SHORT).show()
                        val intentMain = Intent(this@SubActivity_bag, MainActivity2::class.java)
                        startActivity(intentMain)
                    }
                    removeButton.setOnClickListener{
                        Toast.makeText(this@SubActivity_bag, "삭제 항목이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            }


            override fun onCancelled(error: DatabaseError) {
            }


        })


    }

    fun observerData() {
        viewModel.fetchData().observe(this, Observer {

            adapter.setListData(it)
            adapter.notifyDataSetChanged()

        })

    }


    //백키를 눌렀을때
    override fun onBackPressed() {}



//    fun itemDelete(data:Any){
////        myRef!!.removeValue()
//    }

}


//리사이클러뷰 코틀린 설명 잘 되어있는 사이트
//https://blog.yena.io/studynote/2017/12/06/Android-Kotlin-RecyclerView1.html
//리사이클러뷰 파이어베이스 리얼타임베이스
//https://gloria94.tistory.com/19







