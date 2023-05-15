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

class SubPizzaActivity : AppCompatActivity() {

    private val items = mutableListOf<DataClassSubActivity>()
    private var soundPool: SoundPool? = null
    private var sound1 = 0
    private var sound2 = 0
    private var sound3 = 0
    private var sound4 = 0

    private lateinit var pastaLinearLayout: LinearLayout
    private lateinit var appetizerLinearLayout: LinearLayout
    private lateinit var stakeLinearLayout: LinearLayout
    private lateinit var drinkLinearLayout: LinearLayout

    private lateinit var paymentImageView: ImageView

    private lateinit var intentSubPastaActivity: Intent
    private lateinit var intentSubStakeActivity: Intent
    private lateinit var intentSubAppetizerActivity: Intent
    private lateinit var intentSubDrinkActivity: Intent
    private lateinit var intentPaymentListActivity: Intent
    private lateinit var intentSubPizzaItemActivity: Intent

    private lateinit var pizzaSpicy: String
    private lateinit var pizzaJola: String
    private lateinit var pizzaSnowing: String
    private lateinit var pizzaBurata: String
    private lateinit var pizzaGolden: String

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza)

        init()

    }

    private fun init() {

        // 애니매이션
        overridePendingTransition(R.anim.fade_in, R.anim.none)
        //클릭시 소리 사운드
        sound()

        //LinearLayout 변수 선언
        pastaLinearLayout = findViewById(R.id.pastaLinearLayout)
        drinkLinearLayout = findViewById(R.id.drinkLinearLayout)
        appetizerLinearLayout = findViewById(R.id.appetizerLinearLayout)
        stakeLinearLayout = findViewById(R.id.stakeLinearLayout)

        //ImageView 변수 선언
        paymentImageView = findViewById(R.id.paymentImageView)

        //Intent 변수 선언
        intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)
        intentSubStakeActivity = Intent(this, SubStakeActivity::class.java)
        intentSubAppetizerActivity = Intent(this, SubAppetizerActivity::class.java)
        intentSubDrinkActivity = Intent(this, SubDrinkActivity::class.java)
        intentPaymentListActivity = Intent(this, PaymentListActivity::class.java)
        intentSubPizzaItemActivity = Intent(this, SubPizzaItemActivity::class.java)

        //리사이클러뷰 변수 선언
        recyclerView = findViewById(R.id.main3)

        //메세지 가져오기
        pizzaSpicy = this.resources.getString(R.string.pizza_spicy)
        pizzaJola = this.resources.getString(R.string.pizza_jola)
        pizzaSnowing = this.resources.getString(R.string.pizza_snowing)
        pizzaBurata = this.resources.getString(R.string.pizza_burata)
        pizzaGolden = this.resources.getString(R.string.pizza_golden)

        //목록 추가시
        val clickListener = View.OnClickListener {
            val intent = when (it.id) {
                R.id.stakeLinearLayout -> intentSubStakeActivity
                R.id.pastaLinearLayout -> intentSubPastaActivity
                R.id.drinkLinearLayout -> intentSubDrinkActivity
                R.id.appetizerLinearLayout -> intentSubAppetizerActivity
                R.id.paymentImageView -> intentPaymentListActivity
                else -> return@OnClickListener
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            soundPool?.play(sound1, 0.5f, 0.5f, 0, 0, 1f)
            startActivity(intent)
        }
        pastaLinearLayout.setOnClickListener(clickListener)
        drinkLinearLayout.setOnClickListener(clickListener)
        stakeLinearLayout.setOnClickListener(clickListener)
        appetizerLinearLayout.setOnClickListener(clickListener)
        paymentImageView.setOnClickListener(clickListener)

        //리사이클러뷰
        val subActivityAdapter = SubActivityAdapter(this, items)
        recyclerView.adapter = subActivityAdapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        //아이템 추가
        val itemList = listOf(
            DataClassSubActivity(R.drawable.pizza_spicy, Text = pizzaSpicy, 24800),
            DataClassSubActivity(R.drawable.pizza_jola, Text = pizzaJola, 25800),
            DataClassSubActivity(R.drawable.pizza_snowing, Text = pizzaSnowing, 26800),
            DataClassSubActivity(R.drawable.pizza_burata, Text = pizzaBurata, 27500),
            DataClassSubActivity(R.drawable.pizza_golden, Text = pizzaGolden, 27800)
        )
        items.addAll(itemList)

        //Item 클릭 함수
        subActivityAdapter.itemClick = object : SubActivityAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val itemValue = when (items[position].Text) {
                    pizzaSpicy -> 1
                    pizzaJola -> 2
                    pizzaSnowing -> 3
                    pizzaBurata -> 4
                    pizzaGolden -> 5

                    else -> return
                }
                intentSubPizzaItemActivity.putExtra("key", itemValue)
                intentSubPizzaItemActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intentSubPizzaItemActivity)
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