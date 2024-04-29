package com.example.myapplication.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLayoutBinding

class MyViewHolder(val itemView:View) :RecyclerView.ViewHolder(itemView){

    private var binding = ItemLayoutBinding.bind(itemView)
    var txt_cart_name:String = ""
        set(value) {
            field=value
            binding.cartItemName.text=field
        }
    var txt_cart_price:String = ""
        set(value) {
            field=value
            binding.cartItemPrice.text=field
        }


}