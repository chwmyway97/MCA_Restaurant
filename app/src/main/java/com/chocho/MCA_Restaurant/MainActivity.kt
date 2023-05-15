package com.chocho.MCA_Restaurant

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var mainButton :ImageButton

    private lateinit var intentSubPastaActivity :Intent
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mainButton = findViewById(R.id.mainButton)

        intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)

        mainButton.setOnClickListener { startActivity(intentSubPastaActivity) }
    }
    //백키를 눌렀을 때
    override fun onBackPressed() {}
}