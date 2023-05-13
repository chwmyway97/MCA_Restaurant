package com.chocho.MCA_Restaurant


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class PaymentListActivity : AppCompatActivity() {

    private val dec = java.text.DecimalFormat("###,###")

    private var totalAmountSum = 0

    private val client = OkHttpClient()
    private var receivedIntent: Intent? = null
    private val executor = Executors.newSingleThreadScheduledExecutor()

    private val database = Firebase.database
    private val tableDatabase = database.getReference("table")
    private val masterDatabase = database.getReference("master")
    private val table: MutableList<Any> = mutableListOf()


    //리사이클러뷰 변수
    private lateinit var adapter: PaymentlistAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(FirebaseViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)

        // 애니매이션
        overridePendingTransition(R.anim.fade_in, R.anim.none)

        //이미지버튼 변수
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val paymentButton = findViewById<ImageButton>(R.id.payment)
        val removeButton = findViewById<ImageButton>(R.id.removeButton)

        //리사이클러뷰 변수
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)

        //텍스트 뷰 변수
        val totalAmount = findViewById<TextView>(R.id.totalAmount)

        //intent 변수
        val intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)
        val intentMainActivity = Intent(this, MainActivity::class.java)

        //메세지 가져오기
        val deleteMessage = this.resources.getString(R.string.deleteMessage)
        val notPayListMessage = this.resources.getString(R.string.notPayListMessage)
        val notDeleteListMessage = this.resources.getString(R.string.notDeleteListMessage)

        //toast 메세지
        val deleteToast = Toast.makeText(this, deleteMessage, Toast.LENGTH_SHORT)
        val notPayListToast = Toast.makeText(this, notPayListMessage, Toast.LENGTH_SHORT)
        val notDeleteListToast = Toast.makeText(this, notDeleteListMessage, Toast.LENGTH_SHORT)


        //뒤로가기 버튼
        backButton.setOnClickListener {
            intentSubPastaActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intentSubPastaActivity)
        }

        //리사이클러뷰 파이어베이스 연결
        adapter = PaymentlistAdapter(this)

        //RecyclerView 같은 경우 매니져를 설정해 줘야한다.
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        observerData()

        //테이블 데이터베이스에서 가격 정보 가져오기
        tableDatabase.addValueEventListener(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(tableSnapshot: DataSnapshot) {

                if (tableSnapshot.exists()) { //존재할 때
                    //각 meatValue 값 (각 주문한 음식의 가격) 을 다 더함
                    for (tableMeatValue in tableSnapshot.children) {

                        totalAmountSum += tableMeatValue.child("meatValue").getValue(Int::class.java)!!

                    }

                    //결제 버튼(카카오페이)
                    paymentButton.setOnClickListener {

                        kakaoPayReady("1번 테이블", totalAmountSum)

                    }

                    //전체 삭제 버튼
                    removeButton.setOnClickListener {

                        tableDatabase.removeValue()

                        deleteToast.show()

                        startActivity(intentSubPastaActivity)
                    }

                } else { //구매목록이 없을때

                    totalAmountSum = 0

                    paymentButton.setOnClickListener {

                        notPayListToast.show()

                        startActivity(intentSubPastaActivity)

                    }
                    removeButton.setOnClickListener {

                        notDeleteListToast.show()

                    }
                }

                totalAmount.text = "￦ " + dec.format(totalAmountSum)

            }

            override fun onCancelled(error: DatabaseError) {
                val errorMessage = "Database operation cancelled: ${error.message}"
                Toast.makeText(this@PaymentListActivity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observerData() {
        viewModel.fetchData().observe(this, Observer {

            adapter.setListData(it)
            adapter.notifyDataSetChanged()

        })

    }

    fun startPollingPaymentStatus(tid: String) {
        val pollingTask = Runnable { requestPaymentStatus(tid) }
        executor.scheduleAtFixedRate(pollingTask, 0, 1, TimeUnit.SECONDS)
    }

    // 결제 완료 시 폴링 상태 해제
    fun stopPollingPaymentStatus() {
        executor.shutdown()
    }

    // 결제 준비 단계
    private fun kakaoPayReady(name: String, pay: Int) {
        val readyUrl = "https://kapi.kakao.com/v1/payment/ready"
        // 파라미터 설정
        val params = HashMap<String, Any>()
        params["cid"] = "TC0ONETIME"
        params["partner_order_id"] = "123"
        params["partner_user_id"] = "123"
        params["item_name"] = name
        params["quantity"] = "1"
        params["total_amount"] = "$pay"
        params["vat_amount"] = "200"
        params["tax_free_amount"] = "0"
        params["approval_url"] = "http://192.168.0.33:8080/"
        params["fail_url"] = "http://192.168.0.33:8080/"
        params["cancel_url"] = "http://192.168.0.33:8080/"

        // form 형태의 데이터 구성
        val formBody = FormBody.Builder()

        val httpBuilder = Uri.Builder()
        val set: Set<*> = params.keys
        val iterator = set.iterator()
        while (iterator.hasNext()) {

            // body에 데이터 추가
            val key = iterator.next() as String
            formBody.add(key, params[key].toString())
        }

        val request = Request.Builder()
            .addHeader("Authorization", "KakaoAK 471ce1832263a9c17fbec652e3ec12b9")
            .addHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
            .url(readyUrl + httpBuilder.toString())
            .post(formBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i("ready", "실패")
            }

            // TODO [응답을 받은 경우]
            override fun onResponse(call: Call, response: Response) {
                // 카카오페이 결제 요청 결과 처리 부분
                val jsonObject = JSONObject(response.body!!.string())
                Log.d("ready", "" + jsonObject)
                val nextRedirectPcUrl = jsonObject.getString("next_redirect_pc_url")
                val tid = jsonObject.getString("tid")

                // 웹 브라우저로 열기
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nextRedirectPcUrl))
                startActivity(intent)

                startPollingPaymentStatus(tid)
            }
        })
    }

    // 결제 상태 조회
    fun requestPaymentStatus(tid: String) {
        val orderUrl = "https://kapi.kakao.com/v1/payment/order"
        // 요청 바디에 담을 파라미터
        val params = HashMap<String, Any>()
        params["cid"] = "TC0ONETIME"
        params["tid"] = tid

        // form 형태의 데이터 구성
        val formBody = FormBody.Builder()

        val httpBuilder = Uri.Builder()
        val set: Set<*> = params.keys
        val iterator = set.iterator()
        while (iterator.hasNext()) {
            // body에 데이터 추가
            val key = iterator.next() as String
            formBody.add(key, params[key].toString())
        }

        // API 요청
        val request = Request.Builder()
            .addHeader("Authorization", "KakaoAK 471ce1832263a9c17fbec652e3ec12b9")
            .url(orderUrl + httpBuilder)
            .post(formBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("request", "실패")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                // API 요청 성공 시 처리 로직
                if (response.isSuccessful && responseBody != null) {
                    // 결과 JSON 파싱
                    val jsonObject = JSONObject(responseBody)

                    Log.d("success", "" + jsonObject)

                    // pg_token 값을 추출
                    if (receivedIntent != null) {
                        val pgToken: String? = receivedIntent?.data?.getQueryParameter("pg_token")
                        Log.d("token", "" + pgToken)
                        if (pgToken != null) {
                            stopPollingPaymentStatus()
                            kakaoPayApprove(pgToken, tid)
                        }
                    }
                }
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // 새로운 Intent를 받으면 receivedIntent 변수에 저장
        receivedIntent = intent
    }

    // 결제 승인 요청
    private fun kakaoPayApprove(pg_token: String, tid: String) {

        val approveUrl = "https://kapi.kakao.com/v1/payment/approve"
        val params = HashMap<String, Any>()
        params["cid"] = "TC0ONETIME"
        params["tid"] = tid
        params["partner_order_id"] = "123"
        params["partner_user_id"] = "123"
        params["pg_token"] = pg_token

        // form 형태의 데이터 구성
        val formBody = FormBody.Builder()

        val httpBuilder = Uri.Builder()
        val set: Set<*> = params.keys
        val iterator = set.iterator()
        while (iterator.hasNext()) {
            // body에 데이터 추가
            val key = iterator.next() as String
            formBody.add(key, params[key].toString())
        }

        val request = Request.Builder()
            .addHeader("Authorization", "KakaoAK 471ce1832263a9c17fbec652e3ec12b9")
            .url(approveUrl + httpBuilder.toString())
            .post(formBody.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("approve", "실패")
            }

            override fun onResponse(call: Call, response: Response) {
                // 결제 승인 요청 결과 처리
                val responseBody = response.body?.string()
                // API 요청 성공 시 처리 로직
                if (response.isSuccessful && responseBody != null) {
                    // 결과 JSON 파싱
                    val jsonObject = JSONObject(responseBody)
                    Log.d("finish", "" + jsonObject)

                    //결제 결과 master 에 넘김
                    tableDatabase.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(tableSnapshot: DataSnapshot) {

                            table.clear()

                            for (tableMeatValue in tableSnapshot.children) {

                                val tableMeatValueDataDataClass = tableMeatValue.getValue(MeatDataClass::class.java)

                                table.add(tableMeatValueDataDataClass!!)

                                masterDatabase.setValue(table)

                                tableDatabase.removeValue()

                                val intentMainActivity = Intent(this@PaymentListActivity,MainActivity::class.java)
                                startActivity(intentMainActivity)

                                Log.d("테이블 데이터", table.toString())
                            }


                        }


                        override fun onCancelled(error: DatabaseError) {
                        }

                    })



                }

            }
        })
    }

    //백키를 눌렀을 때
    override fun onBackPressed() {}

}


//리사이클러뷰 코틀린 설명 잘 되어있는 사이트
//https://blog.yena.io/studynote/2017/12/06/Android-Kotlin-RecyclerView1.html
//리사이클러뷰 파이어베이스 리얼타임베이스
//https://gloria94.tistory.com/19







