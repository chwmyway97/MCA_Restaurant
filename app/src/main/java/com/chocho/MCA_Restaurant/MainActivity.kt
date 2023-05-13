package com.chocho.MCA_Restaurant

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainButton = findViewById<ImageButton>(R.id.mainButton)

        val intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)

        mainButton.setOnClickListener {

            startActivity(intentSubPastaActivity)
        }
    }
    //백키를 눌렀을 때
    override fun onBackPressed() {}
}