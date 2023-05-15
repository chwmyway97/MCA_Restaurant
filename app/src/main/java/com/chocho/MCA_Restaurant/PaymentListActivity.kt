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
    private val toastShort = Toast.LENGTH_SHORT

    //database
    private val database = Firebase.database
    private val tableDatabase = database.getReference("table")
    private val masterDatabase = database.getReference("master")
    private val table: MutableList<Any> = mutableListOf()

    //kakopay
    private val admin = "KakaoAK 471ce1832263a9c17fbec652e3ec12b9"
    private val approvalUrl = "http://192.168.0.6:8080/"
    private val failUrl = "http://192.168.0.6:8080/"
    private val cancelUrl = "http://192.168.0.6:8080/"

    private val client = OkHttpClient()
    private var receivedIntent: Intent? = null
    private val executor = Executors.newSingleThreadScheduledExecutor()

    //리사이클러뷰 변수
    private lateinit var adapter: PaymentListAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(FirebaseViewModel::class.java) }

    //총 합
    private var totalAmountSum = 0

    private lateinit var backButton: ImageButton
    private lateinit var paymentButton: ImageButton
    private lateinit var removeButton: ImageButton

    private lateinit var totalAmount: TextView

    private lateinit var intentSubPastaActivity: Intent

    private lateinit var paymentListRecyclerView: RecyclerView

    private lateinit var deleteMessage: String
    private lateinit var notPayListMessage: String
    private lateinit var notDeleteListMessage: String

    private lateinit var deleteToast: Toast
    private lateinit var notPayListToast: Toast
    private lateinit var notDeleteListToast: Toast


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_list)

        init()

    }

    private fun init() {
        // 애니매이션
        overridePendingTransition(R.anim.fade_in, R.anim.none)

        //이미지버튼 변수
        backButton = findViewById(R.id.backButton)
        paymentButton = findViewById(R.id.payment)
        removeButton = findViewById(R.id.removeButton)

        //리사이클러뷰 변수
        paymentListRecyclerView = findViewById(R.id.paymentListRecyclerView)

        //텍스트 뷰 변수
        totalAmount = findViewById(R.id.totalAmount)

        //intent 변수
        intentSubPastaActivity = Intent(this, SubPastaActivity::class.java)


        //메세지 가져오기
        deleteMessage = this.resources.getString(R.string.deleteMessage)
        notPayListMessage = this.resources.getString(R.string.notPayListMessage)
        notDeleteListMessage = this.resources.getString(R.string.notDeleteListMessage)

        //toast 메세지
        deleteToast = Toast.makeText(this, deleteMessage, toastShort)
        notPayListToast = Toast.makeText(this, notPayListMessage, toastShort)
        notDeleteListToast = Toast.makeText(this, notDeleteListMessage, toastShort)

        //뒤로가기 버튼
        backButton.setOnClickListener {
            intentSubPastaActivity.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intentSubPastaActivity)
        }

        //리사이클러뷰 파이어베이스 연결
        adapter = PaymentListAdapter(this)

        //RecyclerView 같은 경우 매니져를 설정해 줘야한다.
        paymentListRecyclerView.layoutManager = LinearLayoutManager(this)
        paymentListRecyclerView.setHasFixedSize(true)
        paymentListRecyclerView.adapter = adapter

        observerData()

        //테이블 데이터베이스에서 가격 정보 가져오기
        tableDatabase.addValueEventListener(object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(tableSnapshot: DataSnapshot) {
                totalAmountSum = 0  //totalAmountSum 초기화 안해주면 -누르거나 삭제했을때 계속 값이 추가됨

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
        viewModel.fetchData().observe(this) {

            adapter.setListData(it)
            adapter.notifyDataSetChanged()

        }

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
        params["approval_url"] = approvalUrl
        params["fail_url"] = failUrl
        params["cancel_url"] = cancelUrl

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
            .addHeader("Authorization", admin)
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
    private fun requestPaymentStatus(tid: String) {
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
            .addHeader("Authorization", admin)
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
        // 새로운 Intent 받으면 receivedIntent 변수에 저장
        receivedIntent = intent
    }

    // 결제 승인 요청
    private fun kakaoPayApprove(pg_token: String, tid: String) {

        val intentMainActivity = Intent(this@PaymentListActivity, MainActivity::class.java)

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
            .addHeader("Authorization", admin)
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

                                val tableMeatValueDataDataClass = tableMeatValue.getValue(DataClassMeat::class.java)

                                table.add(tableMeatValueDataDataClass!!)

                                masterDatabase.setValue(table)

                                tableDatabase.removeValue()

                                startActivity(intentMainActivity)

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
/** 키해시
try {
val information =
packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
val signatures = information.signingInfo.apkContentsSigners
val md = MessageDigest.getInstance("SHA")
for (signature in signatures) {
val md: MessageDigest = MessageDigest.getInstance("SHA")
md.update(signature.toByteArray())
var hashcode = String(Base64.encode(md.digest(), 0))
Log.d("hashcode", "" + hashcode)
}
} catch (e: Exception) {
Log.d("hashcode", "에러::" + e.toString())

}
 **/






