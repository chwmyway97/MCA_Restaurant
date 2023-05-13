package com.chocho.MCA_Restaurant

import android.content.Context
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class SubActivityAdapter(val context: Context, val List: MutableList<SubActivityModel>) : RecyclerView.Adapter<SubActivityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubActivityAdapter.ViewHolder{

        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_item,parent,false)

        return ViewHolder(v)
    }
    //리사이 클러뷰 아이템 클릭 이벤트
    interface ItemClick
    {
        fun onClick(view : View, position:Int)

    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder : SubActivityAdapter.ViewHolder, position: Int){
        //리사이 클러뷰 아이템 클릭 이벤트
        if (itemClick!=null){
            holder.itemView.setOnClickListener{ v->
                itemClick!!.onClick(v, position)
            }
        }
        holder.bindItems(List[position])
    }
    override fun getItemCount(): Int {
        return List.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(item : SubActivityModel){
            val orangeImage = itemView.findViewById<ImageView>(R.id.orangeImg)
            val orangeText = itemView.findViewById<TextView>(R.id.orangeText)
            val orangeText2 = itemView.findViewById<TextView>(R.id.orangeText2)

            val dec = DecimalFormat("###,###")
            val money =item.Money
            orangeText.text = item.Text
            orangeText2.text= "￦ " + dec.format(money)
            orangeImage.setImageResource(item.Image)
        }
    }
}