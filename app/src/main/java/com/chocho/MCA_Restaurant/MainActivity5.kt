package com.chocho.MCA_Restaurant

import android.app.ActivityManager
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity5 : AppCompatActivity() {

    private val items = mutableListOf<MainActivityModel>()
    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)
        overridePendingTransition(R.anim.fade_in, R.anim.none) // 애니매이션

        val pastaButton = findViewById<LinearLayout>(R.id.linearLayout1)
        val pizzaButton = findViewById<LinearLayout>(R.id.linearLayout3)
        val appetizerButton = findViewById<LinearLayout>(R.id.linearLayout4)
        val stakeButton = findViewById<LinearLayout>(R.id.linearLayout5)
        val waterButton = findViewById<LinearLayout>(R.id.linearLayout6)
        val bagButton = findViewById<ImageView>(R.id.baguni)

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

        pastaButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
            startActivity(intent)
        }

        pastaButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }


        pizzaButton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }


        appetizerButton.setOnClickListener {
            val intent = Intent(this, MainActivity4::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }



        waterButton.setOnClickListener {
            val intent = Intent(this, MainActivity6::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }


        bagButton.setOnClickListener {
            val intent = Intent(this, SubActivity_bag::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }



        items.add(
            MainActivityModel(
                R.drawable.stake_garic, Text = "갈릭 스테이크", 49500
            )
        )
        items.add(
            MainActivityModel(
                R.drawable.stake_ripeye, Text = "허브 스테이크", 49500
            )
        )
        items.add(
            MainActivityModel(
                R.drawable.stake_hug, Text = "갈릭 허그 스테이크", 49900
            )
        )
        items.add(
            MainActivityModel(
                R.drawable.stake_twist, Text = "본 스테이크", 49500
            )
        )


        val recyclerView = findViewById<RecyclerView>(R.id.main5)
        val mainActivityAdapter = MainActivityAdapter(this, items)
        recyclerView.adapter = mainActivityAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        //클릭함수
        mainActivityAdapter.itemClick = object : MainActivityAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                if (items[position].Text == "갈릭 스테이크") {
                    intent = Intent(this@MainActivity5, MainActivity5Sub::class.java)
                    intent.putExtra("key1", 1)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "허브 스테이크") {
                    intent = Intent(this@MainActivity5, MainActivity5Sub::class.java)
                    intent.putExtra("key1", 2)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "갈릭 허그 스테이크") {
                    intent = Intent(this@MainActivity5, MainActivity5Sub::class.java)
                    intent.putExtra("key1", 3)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "본 스테이크") {
                    intent = Intent(this@MainActivity5, MainActivity5Sub::class.java)
                    intent.putExtra("key1", 4)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }


            }
        }
    }
    //백키를 눌렀을때
    override fun onBackPressed() {}

}