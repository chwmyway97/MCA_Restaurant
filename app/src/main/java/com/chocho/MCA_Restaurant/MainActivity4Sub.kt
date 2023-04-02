package com.chocho.MCA_Restaurant

import android.annotation.SuppressLint
import android.app.ActivityManager
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

class MainActivity4Sub : AppCompatActivity() {

    var number = 1
    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build()
        } else { SoundPool(6, AudioManager.STREAM_MUSIC, 0) }
        sound1 = soundPool!!.load(this, R.raw.push, 1)
        sound2 = soundPool!!.load(this, R.raw.sound2, 1)
        sound3 = soundPool!!.load(this, R.raw.sound3, 1)
        sound4 = soundPool!!.load(this, R.raw.sound4, 1)


        overridePendingTransition(R.anim.fade_in, R.anim.none)
        val value = intent.getIntExtra("key1",0)
        Log.d("값", "$value")
        if (value == 1) {
            setContentView(R.layout.activity_main_sub4_1)
            //증감
            val plusButton = findViewById<ImageButton>(R.id.plusButton)
            val minusButton = findViewById<ImageButton>(R.id.minusButton)
            val pmText = findViewById<TextView>(R.id.textNumber)
            plusButton.setOnLongClickListener {
                handler1_up.post(runnable1_up)
                false
            }
            minusButton.setOnLongClickListener {
                handler1_down.post(runnable1_down)
                false
            }
            plusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number++
                    if (number >= 10) {
                        number = 10
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_up.removeCallbacks(runnable1_up)
                }
                false

            }
            minusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number--
                    if (number <= 1) {
                        number = 1
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_down.removeCallbacks(runnable1_down)
                }
                false

            }

            //실시간 데이터 베이스
            val bringButton = findViewById<ImageButton>(R.id.bringButton)
            bringButton.setOnClickListener{
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                val database = Firebase.database
                val myRef = database.getReference("table")
                myRef.child("13").setValue(Meat("시저 샐러드",number,number*19500))
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)


            }

            //백버튼
            val backButton = findViewById<ImageButton>(R.id.backButton_white)
            backButton.setOnClickListener {
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
        if (value == 2) {
            setContentView(R.layout.activity_main_sub4_2)
            //증감
            val plusButton = findViewById<ImageButton>(R.id.plusButton)
            val minusButton = findViewById<ImageButton>(R.id.minusButton)
            val pmText = findViewById<TextView>(R.id.textNumber)
            plusButton.setOnLongClickListener {
                handler1_up.post(runnable1_up)
                false
            }
            minusButton.setOnLongClickListener {
                handler1_down.post(runnable1_down)
                false
            }
            plusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number++
                    if (number >= 10) {
                        number = 10
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_up.removeCallbacks(runnable1_up)
                }
                false

            }
            minusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number--
                    if (number <= 1) {
                        number = 1
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_down.removeCallbacks(runnable1_down)
                }
                false

            }

            //실시간 데이터 베이스
            val bringButton = findViewById<ImageButton>(R.id.bringButton)
            bringButton.setOnClickListener{
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                val database = Firebase.database
                val myRef = database.getReference("table")
                myRef.child("14").setValue(Meat("갈릭 타워 샐러드",number,number*19900))
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)


            }
            //백버튼
            val backButton = findViewById<ImageButton>(R.id.backButton_white)
            backButton.setOnClickListener {
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }

        if (value == 3) {
            setContentView(R.layout.activity_main_sub4_3)
            //증감
            val plusButton = findViewById<ImageButton>(R.id.plusButton)
            val minusButton = findViewById<ImageButton>(R.id.minusButton)
            val pmText = findViewById<TextView>(R.id.textNumber)
            plusButton.setOnLongClickListener {
                handler1_up.post(runnable1_up)
                false
            }
            minusButton.setOnLongClickListener {
                handler1_down.post(runnable1_down)
                false
            }
            plusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number++
                    if (number >= 10) {
                        number = 10
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_up.removeCallbacks(runnable1_up)
                }
                false

            }
            minusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number--
                    if (number <= 1) {
                        number = 1
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_down.removeCallbacks(runnable1_down)
                }
                false

            }

            //실시간 데이터 베이스
            val bringButton = findViewById<ImageButton>(R.id.bringButton)
            bringButton.setOnClickListener{
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                val database = Firebase.database
                val myRef = database.getReference("table")
                myRef.child("15").setValue(Meat("케일 샐러드",number,number*19900))
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)

            }
            //백버튼
            val backButton = findViewById<ImageButton>(R.id.backButton_white)
            backButton.setOnClickListener {
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }

        if (value == 4) {
            setContentView(R.layout.activity_main_sub4_4)
            //증감
            val plusButton = findViewById<ImageButton>(R.id.plusButton)
            val minusButton = findViewById<ImageButton>(R.id.minusButton)
            val pmText = findViewById<TextView>(R.id.textNumber)
            plusButton.setOnLongClickListener {
                handler1_up.post(runnable1_up)
                false
            }
            minusButton.setOnLongClickListener {
                handler1_down.post(runnable1_down)
                false
            }
            plusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)

                    number++
                    if (number >= 10) {
                        number = 10
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_up.removeCallbacks(runnable1_up)
                }
                false

            }
            minusButton.setOnTouchListener {_,event->
                //터치 이벤트 리스너 등록(누를때)
                if (event.action == MotionEvent.ACTION_DOWN) { //눌렀을 때 동작
                    soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                    number--
                    if (number <= 1) {
                        number = 1
                    }
                    pmText.text = number.toString()
                }
                if (event.action == MotionEvent.ACTION_UP) { //뗐을 때 동작
                    handler1_down.removeCallbacks(runnable1_down)
                }
                false

            }

            //실시간 데이터 베이스
            val bringButton = findViewById<ImageButton>(R.id.bringButton)
            bringButton.setOnClickListener{
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                val database = Firebase.database
                val myRef = database.getReference("table")
                myRef.child("16").setValue(Meat("쉬림프 카슈엘라",number,number*25900))
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)

            }

            //백버튼
            val backButton = findViewById<ImageButton>(R.id.backButton_white)
            backButton.setOnClickListener {
                soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
                intent = Intent(this, MainActivity4::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
            }
        }
    }
    private val handler1_up: Handler = Handler()
    private val runnable1_up: Runnable = object : Runnable {
        override fun run() {
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
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
    //백키를 눌렀을때
    override fun onBackPressed() {}

}