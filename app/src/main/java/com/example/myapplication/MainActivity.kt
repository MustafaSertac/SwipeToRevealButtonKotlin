package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Adapter.MyAdapter
import com.example.myapplication.Helper.MyButton
import com.example.myapplication.Helper.MySwipeHelper
import com.example.myapplication.Listener.MyButtonOnClickListener
import com.example.myapplication.Model.Item
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    private lateinit var layoutManager:LinearLayoutManager
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.setHasFixedSize(true)
        layoutManager=LinearLayoutManager(this)
        binding.recyclerView.layoutManager=layoutManager
        val swipe=object:MySwipeHelper(applicationContext,binding.recyclerView,200){
            override fun instatientMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
               buffer.add(MyButton(applicationContext,"Delete",30,0, Color.parseColor("#FF3C30"),object:MyButtonOnClickListener{
                   override fun onClick(pos: Int) {
                      Log.e("tag",pos.toString()+" . item was deleted")
                   }

               }))

            }

        }
        generateItem()


    }

    private fun generateItem() {

        val itemList=ArrayList<Item>()
        for(i in 0..50){
            itemList.add(Item(i.toString(), "Etin Kilosu olmuş  $i"))
        }
        adapter= MyAdapter(this,itemList)
        binding.recyclerView.adapter=adapter
    }

}