package com.chocho.MCA_Restaurant

import android.annotation.SuppressLint
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

class SubStakeActivity : AppCompatActivity() {

    private val items = mutableListOf<DataClassSubActivity>()
    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    private lateinit var pastaLinearLayout: LinearLayout
    private lateinit var pizzaLinearLayout: LinearLayout
    private lateinit var appetizerLinearLayout: LinearLayout
    private lateinit var drinkLinearLayout: LinearLayout

    private lateinit var paymentImageView: ImageView

    private lateinit var intentSubPastaActivity: Intent
    private lateinit var intentSubPizzaActivity: Intent
    private lateinit var intentSubDrinkActivity: Intent
    private lateinit var intentSubAppetizerActivity: Intent
    private lateinit var intentPaymentListActivity: Intent
    private lateinit var intentSubStakeItemActivity: Intent

    private lateinit var stakeGarlic: String
    private lateinit var stakeHub: String
    private lateinit var stakeHug: String
    private lateinit var stakeTwist: String

    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stake)

        init()

    }

    private fun init() {

        // 애니매이션
        overridePendingTransition(R.anim.fade_in, R.anim.none)

        //클릭시 소리 사운드
        sound()

        //LinearLayout 변수 선언
        pastaLinearLayout = findViewById(R.id.pastaLinearLayout)
        pizzaLinearLayout = findViewById(R.id.pizzaLinearLayout)
        appetizerLinearLayout = findViewById(R.id.appetizerLinearLayout)
        drinkLinearLayout = findViewById(R.id.drinkLinearLayout)

        //ImageView 변수 선언
        paymentImageView = findViewById(R.id.paymentImageView)

        //Intent 변수 선언
        intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)
        intentSubPizzaActivity = Intent(this, SubPizzaActivity::class.java)
        intentSubDrinkActivity = Intent(this, SubStakeActivity::class.java)
        intentSubAppetizerActivity = Intent(this, SubAppetizerActivity::class.java)
        intentSubDrinkActivity = Intent(this, SubDrinkActivity::class.java)
        intentPaymentListActivity = Intent(this, PaymentListActivity::class.java)
        intentSubStakeItemActivity = Intent(this, SubStakeItemActivity::class.java)

        //리사이클러뷰 변수 선언
        recyclerView = findViewById(R.id.main5)

        //메세지 가져오기
        stakeGarlic = this.resources.getString(R.string.stake_garlic)
        stakeHub = this.resources.getString(R.string.stake_hub)
        stakeHug = this.resources.getString(R.string.stake_hug)
        stakeTwist = this.resources.getString(R.string.stake_twist)

        //목록 추가시
        val clickListener = View.OnClickListener {
            val intent = when (it.id) {
                R.id.pastaLinearLayout -> intentSubPastaActivity
                R.id.pizzaLinearLayout -> intentSubPizzaActivity
                R.id.appetizerLinearLayout -> intentSubAppetizerActivity
                R.id.drinkLinearLayout -> intentSubDrinkActivity
                R.id.paymentImageView -> intentPaymentListActivity
                else -> return@OnClickListener
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }
        pastaLinearLayout.setOnClickListener(clickListener)
        pizzaLinearLayout.setOnClickListener(clickListener)
        appetizerLinearLayout.setOnClickListener(clickListener)
        drinkLinearLayout.setOnClickListener(clickListener)
        paymentImageView.setOnClickListener(clickListener)

        //리사이클러뷰
        val subActivityAdapter = SubActivityAdapter(this, items)
        recyclerView.adapter = subActivityAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        //아이템 추가
        val itemList = listOf(
            DataClassSubActivity(R.drawable.stake_garlic, Text = stakeGarlic, 49500),
            DataClassSubActivity(R.drawable.stake_hub, Text = stakeHub, 49500),
            DataClassSubActivity(R.drawable.stake_hug, Text = stakeHug, 49900),
            DataClassSubActivity(R.drawable.stake_twist, Text = stakeTwist, 49500),
        )
        items.addAll(itemList)

        //Item 클릭 함수
        subActivityAdapter.itemClick = object : SubActivityAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val itemValue = when (items[position].Text) {
                    stakeGarlic -> 1
                    stakeHub -> 2
                    stakeHug -> 3
                    stakeTwist -> 4
                    else -> return
                }
                intentSubStakeItemActivity.putExtra("key", itemValue)
                intentSubStakeItemActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intentSubStakeItemActivity)
            }
        }

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