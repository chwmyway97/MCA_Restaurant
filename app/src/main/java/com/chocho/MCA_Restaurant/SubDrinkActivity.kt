package com.chocho.MCA_Restaurant

import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SubDrinkActivity : AppCompatActivity() {

    private val items = mutableListOf<SubActivityModel>()
    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main6)
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
            val intent = Intent(this, SubPastaActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)

            startActivity(intent)
        }

        pizzaButton.setOnClickListener {
            val intent = Intent(this, SubPizzaActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)

            startActivity(intent)
        }

        appetizerButton.setOnClickListener {
            val intent = Intent(this, SubAppetizerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)

            startActivity(intent)
        }

        stakeButton.setOnClickListener {
            val intent = Intent(this, SubStakeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
            startActivity(intent)
        }

        bagButton.setOnClickListener {
            val intent = Intent(this, PaymentListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1,0.5f,0.5f,0,0,1f)
            startActivity(intent)
        }

        items.add(
            SubActivityModel(
                R.drawable.water_orange, Text = "오렌지 에이드" , 6900
            )
        )

        items.add(
            SubActivityModel(
                R.drawable.water_lemon, Text = "레몬 에이드", 6900
            )
        )
        items.add(
            SubActivityModel(
                R.drawable.water_jamong, Text = "자몽 에이드", 6900
            )
        )
        items.add(
            SubActivityModel(
                R.drawable.water_red, Text = "레드 와인 쿨러", 6900
            )
        )
        items.add(
            SubActivityModel(
                R.drawable.water_white, Text = "화이트 와인 쿨러", 6900
            )
        )
        items.add(
            SubActivityModel(
                R.drawable.water_7star, Text = "사이다", 4500
            )
        )
        
        val recyclerView = findViewById<RecyclerView>(R.id.main6)
        val subActivityAdapter = SubActivityAdapter(this, items)
        recyclerView.adapter = subActivityAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)



//클릭함수
        subActivityAdapter.itemClick = object : SubActivityAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                if (items[position].Text == "오렌지 에이드") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 1)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "레몬 에이드") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 2)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "자몽 에이드") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 3)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "레드 와인 쿨러") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 4)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "화이트 와인 쿨러") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 5)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }
                if (items[position].Text == "사이다") {
                    intent = Intent(this@SubDrinkActivity, SubDringItemActivity::class.java)
                    intent.putExtra("key1", 6)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                }

            }
        }
    }
    //백키를 눌렀을때
    override fun onBackPressed() {}

}

