package com.chocho.MCA_Restaurant


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color.BLACK
import android.icu.text.DecimalFormat
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay


class SublistAdapter(private val context: Context) :
    RecyclerView.Adapter<SublistAdapter.ViewHolder>(){

    private var meatList = mutableListOf<Meat>()



    fun setListData(data: MutableList<Meat>) {
        meatList = data
    }

    //화면을 최초 로딩하여 만들어진 View 가 없는 경우,xml 퍼알을 inflate 하여 ViewHolder 를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SublistAdapter.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.activity_bag_item, parent, false)
        return ViewHolder(view)
    }


    //onBindViewHolder : 위의 onCreateViewHolder 에서 만든 view 와 실제 입력되는 각각의 데이터를 연결한다.
    override fun onBindViewHolder(
        holder: SublistAdapter.ViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val dec = DecimalFormat("###,###")
        val meat: Meat = meatList[position]
        val money = meat.meatValue
        var count = meat.meatNumber

        holder.textName.text = meat.meatMenu
        holder.textNumber.text = count.toString()
        holder.textValue.text ="￦ " + dec.format(money)

        //+-선택창
        fun ho(text: String, num: String,value:Int) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("table")

            if (holder.textName.text == text) {

                holder.plusButton.setOnClickListener {
                    count++
                    if (count >= 10) {
                        count = 10
                    }
                    myRef.child(num).setValue(Meat(text, count, count * value))
                }
                holder.minusButton.setOnClickListener {
                    count--
                    if (count <= 1) {
                        count = 1
                    }

                    myRef.child(num).setValue(Meat(text, count, count * value))
                }

            }
            Log.d("s", count.toString())
            Log.d("ss", (count * 24800).toString())
        }

        //삭제창
        fun hoho(text: String, num: String) {
            if (holder.textName.text == text) {
                holder.btn.setOnClickListener() {
                    val builder = AlertDialog.Builder(holder.itemView.context)


                    builder.setMessage("선택한 상품을 삭제하시겠습니까?")

                        .setCancelable(true)
                        .setPositiveButton("Yes") { _, _ ->
                            // Delete from firebase
                            val db = FirebaseDatabase.getInstance()
                            val ref = db.getReference("table")
                            ref.child(num).removeValue()
                            meatList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            // Dismiss the dialog
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()

                }

            }
        }


        ho("갈릭 페뇨 파스타", "1",24800)
        ho("트리플 갈릭 파스타", "2",24800)
        ho("갈릭 까르보나라", "3",25500)
        ho("아 라 비 아 따", "4",25500)
        ho("명란 파스타", "5",26200)
        ho("해산물 수프 파스타", "6",26200)
        ho("랍스타 크림 파스타", "7",27500)
        ho("단호박 크림 피자", "8",24800)
        ho("고르곤졸라 피자", "9",25800)
        ho("갈릭 스노윙 피자", "10",26800)
        ho("마르&부라타 피자", "11",27500)
        ho("바질 부라타 피자", "12",27800)
        ho("시저 샐러드", "13",19500)
        ho("갈릭 타워 샐러드", "14",19900)
        ho("케일 샐러드", "15",19900)
        ho("쉬림프 카슈엘라", "16",25900)
        ho("갈릭 스테이크", "17",49500)
        ho("허브 스테이크", "18",49500)
        ho("갈릭 허그 스테이크", "19",49900)
        ho("본 스테이크", "20",49500)
        ho("오 렌 지 에 이 드", "21",6900)
        ho("레 몬 에 이 드", "22",6900)
        ho("자 몽 에 이 드", "23",6900)
        ho("레 드 와 인 쿨 러", "24",6900)
        ho("화 이 트 와 인 쿨 러", "25",6900)
        ho(" 사 이 다 ", "26",4500)


        hoho("갈릭 페뇨 파스타", "1")
        hoho("트리플 갈릭 파스타", "2")
        hoho("갈릭 까르보나라", "3")
        hoho("아 라 비 아 따", "4")
        hoho("명란 파스타", "5")
        hoho("해산물 수프 파스타", "6")
        hoho("랍스타 크림 파스타", "7")
        hoho("단호박 크림 피자", "8")
        hoho("고르곤졸라 피자", "9")
        hoho("갈릭 스노윙 피자", "10")
        hoho("마르&부라타 피자", "11")
        hoho("바질 부라타 피자", "12")
        hoho("시저 샐러드", "13")
        hoho("갈릭 타워 샐러드", "14")
        hoho("케일 샐러드", "15")
        hoho("쉬림프 카슈엘라", "16")
        hoho("갈릭 스테이크", "17")
        hoho("허브 스테이크", "18")
        hoho("갈릭 허그 스테이크", "19")
        hoho("본 스테이크", "20")
        hoho("오 렌 지 에 이 드", "21")
        hoho("레 몬 에 이 드", "22")
        hoho("자 몽 에 이 드", "23")
        hoho("레 드 와 인 쿨 러", "24")
        hoho("화 이 트 와 인 쿨 러", "25")
        hoho(" 사 이 다 ", "26")



//        if (holder.textName.text == "트리플 갈릭 파스타") {
//            holder.btn.setOnClickListener() {
//                val builder = AlertDialog.Builder(holder.itemView.context)
//                builder.setMessage("Are you sure you want to Delete?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes") { _, _ ->
//                        // Delete from firebase
//                        val db = FirebaseDatabase.getInstance()
//                        val ref = db.getReference("table")
//                        ref.child("2").removeValue()
//                        meatList.removeAt(position)
//                        notifyItemRemoved(position)
//                    }
//                    .setNegativeButton("No") { dialog, _ ->
//                        // Dismiss the dialog
//                        dialog.dismiss()
//                    }
//                val alert = builder.create()
//                alert.show()
//            }
//        }


    }


    override fun getItemCount(): Int {
        return meatList.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textName: TextView = itemView.findViewById(R.id.textName)
        val textValue: TextView = itemView.findViewById(R.id.textValue)
        val textNumber: TextView = itemView.findViewById(R.id.textNumber)
        val btn: ImageButton = itemView.findViewById(R.id.delete)
        val plusButton: ImageButton = itemView.findViewById(R.id.plusButton)
        val minusButton: ImageButton = itemView.findViewById(R.id.minusButton)



    }


}

