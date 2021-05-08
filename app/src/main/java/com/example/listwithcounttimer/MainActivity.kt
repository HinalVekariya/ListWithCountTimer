package com.example.listwithcounttimer

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class MainActivity : AppCompatActivity() {
    private val counterModelList = arrayListOf<CounterModel>()
    private var id:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val adapter = CountTimerAdapter(counterModelList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        btnAdd.setOnClickListener {
            val model = CounterModel(id,0, "0", getString(R.string.start_btn_text))
            counterModelList.add(model)
            adapter.notifyDataSetChanged()
            id++

        }

    }

    class CountTimerAdapter(private var counterModelList: ArrayList<CounterModel>) :
        RecyclerView.Adapter<CountTimerAdapter.TimerViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
            return TimerViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return counterModelList.size
        }

        override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
            if (counterModelList.isNotEmpty()) {
                holder.itemView.apply {
                    val model=counterModelList[position]
                    btn.text = model.btnText
                    var isTimerRunning = false
                    var isTimerStarted = false
                    var timeInMillis: Long = 0
                    var seconds = 0
                    var hours = 0
                    var minutes = 0
                    lateinit var countDownTimer: CountDownTimer

                    btn.setOnClickListener {
                        if(evSec.text.toString().isEmpty()){
                            Toast.makeText(context,"Please Enter second",Toast.LENGTH_SHORT).show()
                        }else {
                            var enteredSec: Int = evSec.text.toString().toInt()
                            model.sec=enteredSec
                            if (!isTimerStarted) {
                                //Setting values when user enters seconds for the first time
                                timeInMillis = enteredSec * 1000L
                                seconds = enteredSec % 60
                                hours = enteredSec / 60
                                minutes = hours % 60
                                hours /= 60
                            }
                            isTimerRunning = !isTimerRunning
                            if (isTimerRunning) {
                                evSec.isEnabled=false
                                tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                                model.timerValue=tvTimer.text.toString()

                                countDownTimer = object : CountDownTimer(timeInMillis, 1000) {
                                    override fun onFinish() {
                                        //Here timer is finished
                                        btn.text = context.getString(R.string.start_btn_text)
                                        model.btnText=btn.text.toString()
                                        isTimerStarted=false
                                        isTimerRunning=false
                                        evSec.isEnabled=true
                                    }

                                    override fun onTick(p0: Long) {
                                        //Here timer is running
                                        //Need to convert p0 (current count) to HH:mm:ss format
                                        timeInMillis = p0
                                        enteredSec = (timeInMillis / 1000).toInt()
                                        seconds = enteredSec % 60
                                        hours = enteredSec / 60
                                        minutes = hours % 60
                                        hours /= 60
                                        tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                                        model.timerValue=tvTimer.text.toString()
                                    }

                                }.start()
                                btn.text = context.getString(R.string.pause_btn_text)
                                model.btnText=btn.text.toString()
                                isTimerRunning = true
                                isTimerStarted=true

                            } else {
                                //Here timer is in "Pause" mode
                                countDownTimer.cancel()
                                isTimerRunning = false
                                btn.text = context.getString(R.string.resume_btn_text)
                                model.btnText=btn.text.toString()

                            }
                        }

                    }
                }
            }

        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemViewType(position: Int): Int {
            return position
        }
        

        inner class TimerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}