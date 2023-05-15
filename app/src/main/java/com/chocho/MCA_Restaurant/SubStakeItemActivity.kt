package com.chocho.MCA_Restaurant

import android.annotation.SuppressLint
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SubStakeItemActivity : AppCompatActivity() {

    private lateinit var plusButton: ImageButton
    private lateinit var minusButton: ImageButton
    private lateinit var bringButton: ImageButton
    private lateinit var backButtonWhite: ImageButton

    private lateinit var textNumber: TextView

    //string 가져오기
    private lateinit var stakeGarlic: String
    private lateinit var stakeHub: String
    private lateinit var stakeHug: String
    private lateinit var stakeTwist: String

    private lateinit var itemName: String
    private var itemPrice = 0
    private var number = 1

    //database
    private val database = Firebase.database
    private val tableDatabase = database.getReference("table")

    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    private val handler: Handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            soundPool!!.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            val pmText = findViewById<TextView>(R.id.textNumber)
            number += if (isIncreasing) 1 else -1
            number = number.coerceIn(1, 10)
            pmText.text = number.toString()
            handler.postDelayed(this, 100)
        }
    }

    private var isIncreasing: Boolean = false

    private val handler1_up: Handler = Handler()
    private val runnable1_up: Runnable = object : Runnable {
        override fun run() {
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            val pmText = findViewById<TextView>(R.id.textNumber)
            // Print out your letter here...
            number++
            if (number >= 10) {
                number = 10
            }
            // Call the runnable again
            handler1_up.postDelayed(this, 100)
            pmText.text = number.toString()
        }
    }
    private val handler1_down: Handler = Handler()
    private val runnable1_down: Runnable = object : Runnable {
        override fun run() {
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            val pmText = findViewById<TextView>(R.id.textNumber)
            // Print out your letter here...
            number--
            if (number <= 1) {
                number = 1
            }

            // Call the runnable again
            handler1_down.postDelayed(this, 100)
            pmText.text = number.toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(){

        //전 화면에서 값이 넘어 오는것 받기
        val itemValue = intent.getIntExtra("key", 0)

        //애니매이션
        overridePendingTransition(R.anim.fade_in, R.anim.none)

        //소리
        sound()

        //값 넘어오는 것에 따라 activity 선택
        when (itemValue) {
            1 -> setContentView(R.layout.activity_sub_stake_garlic)
            2 -> setContentView(R.layout.activity_sub_stake_hub)
            3 -> setContentView(R.layout.activity_sub_stake_hug)
            4 -> setContentView(R.layout.activity_sub_stake_twist)


        }

        //변수 선언
        textNumber = findViewById(R.id.textNumber)
        plusButton = findViewById(R.id.plusButton)
        minusButton = findViewById(R.id.minusButton)
        bringButton = findViewById(R.id.bringButton)
        backButtonWhite = findViewById(R.id.backButtonWhite)

        //메세지 가져오기
        stakeGarlic = this.resources.getString(R.string.stake_garlic)
        stakeHub = this.resources.getString(R.string.stake_hub)
        stakeHug = this.resources.getString(R.string.stake_hug)
        stakeTwist = this.resources.getString(R.string.stake_twist)

        //plus minus button
        plusButton.setOnLongClickListener {
            handler1_up.post(runnable1_up)
            false
        }

        minusButton.setOnLongClickListener {
            handler1_down.post(runnable1_down)
            false
        }

        plusButton.setOnTouchListener { _, event -> handleButtonTouchEvent(event, isPlusButton = true) }

        minusButton.setOnTouchListener { _, event -> handleButtonTouchEvent(event, isPlusButton = false) }

        bringButton.setOnClickListener {
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)

            when (itemValue) {
                1 -> {
                    itemName = stakeGarlic
                    itemPrice = number * 49500
                    tableDatabase.child("17").setValue(DataClassMeat(itemName, number, itemPrice))
                }
                2 -> {
                    itemName = stakeHub
                    itemPrice = number * 49500
                    tableDatabase.child("18").setValue(DataClassMeat(itemName, number, itemPrice))
                }
                3 -> {
                    itemName = stakeHug
                    itemPrice = number * 49900
                    tableDatabase.child("19").setValue(DataClassMeat(itemName, number, itemPrice))
                }
                4 -> {
                    itemName = stakeTwist
                    itemPrice = number * 49500
                    tableDatabase.child("20").setValue(DataClassMeat(itemName, number, itemPrice))
                }

                else -> return@setOnClickListener
            }

            intent = Intent(this, SubStakeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }

        backButtonWhite.setOnClickListener {
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            intent = Intent(this, SubStakeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }


    }

    private fun handleButtonTouchEvent(event: MotionEvent, isPlusButton: Boolean): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            if (isPlusButton) {
                number++
                if (number >= 10) {
                    number = 10
                }
            } else {
                number--
                if (number <= 1) {
                    number = 1
                }
            }
            textNumber.text = number.toString()
        } else if (event.action == MotionEvent.ACTION_UP) {
            if (isPlusButton) {
                handler1_up.removeCallbacks(runnable1_up)
            } else {
                handler1_down.removeCallbacks(runnable1_down)
            }
        }
        return false
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun sound() {





        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            SoundPool(6, AudioManager.STREAM_MUSIC, 0)
        }
        sound1 = soundPool!!.load(this, R.raw.push, 1)
        sound2 = soundPool!!.load(this, R.raw.sound2, 1)
        sound3 = soundPool!!.load(this, R.raw.sound3, 1)
        sound4 = soundPool!!.load(this, R.raw.sound4, 1)

    }


    //백키를 눌렀을 때
    override fun onBackPressed() {}

}