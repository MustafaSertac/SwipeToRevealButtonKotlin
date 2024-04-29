package com.example.myapplication.Helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import java.util.ArrayList
import java.util.LinkedList

abstract class MySwipeHelper(
    context: Context,
    private val recyclerView: RecyclerView,
    internal var buttonWidth: Int
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var buttonList: MutableList<MyButton>? = null
    lateinit var gestureDetector: GestureDetector
    var swipePosition = -1
    var swipeThreshold = 0.5f
    var buttonBuffer: MutableMap<Int, MutableList<MyButton>>
    lateinit var removerQueue: LinkedList<Int>

    abstract fun instatientMyButton(
        viewHolder: RecyclerView.ViewHolder,
        buffer: MutableList<MyButton>
    )

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (button in buttonList!!) {
                if (button.onClick(e.x, e.y))
                    break
            }

            return true
        }

    }
    private val onTouchListener = View.OnTouchListener { _, motionEvent ->
        if (swipePosition < 0) {
            return@OnTouchListener false
        }
        val point = Point(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
        val swipeViewHolder = recyclerView.findViewHolderForPosition(swipePosition)
        val swipeItem = swipeViewHolder!!.itemView
        val rect = Rect()
        swipeItem.getGlobalVisibleRect(rect)
        if (motionEvent.action == MotionEvent.ACTION_DOWN || motionEvent.action == MotionEvent.ACTION_UP || motionEvent.action == MotionEvent.ACTION_MOVE) {
            if (rect.top < point.y || rect.bottom > point.y) {
                gestureDetector.onTouchEvent(motionEvent)
            } else {
                removerQueue.add(swipePosition)
                swipePosition = -1
                recoverySwipeItem()
            }
        }
        false
    }

    @Synchronized
    private fun recoverySwipeItem() {
        while (!removerQueue.isEmpty()) {
            val pos = removerQueue.poll().toInt()
            if (pos > -1) {
                recyclerView.adapter!!.notifyItemChanged(pos)
            }
        }

    }

    init {

        this.buttonList = ArrayList()
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer = HashMap()
        this.removerQueue = IntLinkedList()
        attachSwipe()
    }

    private fun attachSwipe() {
       val itemTouchHelper=ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
       return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos=viewHolder.adapterPosition
        if(swipePosition!=pos){
            removerQueue.add(swipePosition)
        }
        swipePosition=pos
        if(buttonBuffer.contains(swipePosition))
            buttonList=buttonBuffer[swipePosition]
        else
            buttonList!!.clear()
        buttonBuffer.clear()
        swipeThreshold=0.5f*buttonList!!.size.toFloat()*buttonWidth.toFloat()
        recoverySwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: ViewHolder): Float {
        return swipeThreshold
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 0.1f*defaultValue
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 0.5f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos=viewHolder.adapterPosition
        var translationX=dX
        var itemView=viewHolder.itemView
        if(pos<0){
            swipePosition=0
            return
        }
         if(actionState==ItemTouchHelper.ACTION_STATE_SWIPE){
             if(dX<0){
                 var buffer:MutableList<MyButton> = ArrayList()
                 if(!buttonBuffer.contains(pos)){
                     instatientMyButton(viewHolder,buffer)
                     buttonBuffer[pos]=buffer
                 }
                 else {
                     buffer=buttonBuffer[pos]!!

                 }
                 translationX=dX*buffer.size.toFloat()*buttonWidth.toFloat()/itemView.width
                 drawButton(c,itemView,buffer,pos,translationX)
             }
         }
        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<MyButton>, pos: Int, translationX: Float) {
    var right=itemView.right.toFloat()
        val dButtonWidth=-1*translationX/buffer.size

        for(button in buffer){
            val left=right-dButtonWidth
            button.onDraw(c, RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
            right=left
        }
    }


}

class IntLinkedList : LinkedList<Int>() {
    override fun contains(element: Int): Boolean {
        return false
    }

    override fun lastIndexOf(element: Int): Int {
        return element
    }

    override fun remove(element: Int): Boolean {
        return false
    }

    override fun indexOf(element: Int): Int {
        return element
    }

    override fun add(element: Int): Boolean {
        return if (contains(element))
            false
        else
            super.add(element)
    }


}
