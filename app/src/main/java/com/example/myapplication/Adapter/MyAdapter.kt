package com.example.myapplication.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.Item
import com.example.myapplication.R

class MyAdapter(internal  var context: Context,internal var itemList:MutableList<Item>):RecyclerView.Adapter<MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
         return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txt_cart_name=itemList.get(position).text_name
        holder.txt_cart_price=itemList.get(position).priceName

    }
}