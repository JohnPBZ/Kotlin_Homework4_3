package com.example.kotlin_homework4_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val scope = CoroutineScope(Dispatchers.Default)
    private var p=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btn_calculate = findViewById<Button>(R.id.btn_calculate)
        var ed_height = findViewById<EditText>(R.id.ed_height)
        var ed_weight = findViewById<EditText>(R.id.ed_height)
        btn_calculate.setOnClickListener {
            btn_calculate.isEnabled=false
            when {
                ed_height.length() < 1 -> Toast.makeText(this, "Please Input Height", Toast.LENGTH_SHORT).show()
                ed_weight.length() < 1 -> Toast.makeText(this, "Please Input Weight", Toast.LENGTH_SHORT).show()
                else ->
                    scope.launch {
                        myCoroutineTask()
                    }
            }

        }
    }
    //開始進入協程
    private suspend fun myCoroutineTask(){
        var tv_bmi = findViewById<TextView>(R.id.tv_bmi)
        var tv_weight = findViewById<TextView>(R.id.tv_weight)
        var progressBar2 = findViewById<ProgressBar>(R.id.progressBar2)
        var tv_progress = findViewById<TextView>(R.id.tv_progress)
        var ll_progress = findViewById<LinearLayout>(R.id.ll_progress)
        var ed_height = findViewById<EditText>(R.id.ed_height)
        var ed_weight = findViewById<EditText>(R.id.ed_height)
        var btn_boy = findViewById<RadioButton>(R.id.btn_boy)
        var btn_calculate = findViewById<Button>(R.id.btn_calculate)
        try{
            //onPreExecute
            withContext(Dispatchers.Main){
                tv_bmi.text="體脂肪\n無"
                tv_weight.text="標準體重\n無"
                progressBar2.progress=0
                p=0
                tv_progress.text="0%"
                ll_progress.visibility= View.VISIBLE
            }

            //doInBackground
            while (p<=100) {
                try {
                    Thread.sleep(50)
                    p++
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                //onProgressUpdate
                progressBar2.progress = p
                runOnUiThread{tv_progress.text="$p %"}
            }

            //onPostExecute
            withContext(Dispatchers.Main) {
                ll_progress.visibility = View.GONE
                val height = ed_height.text.toString().toDouble()
                val weight = ed_weight.text.toString().toDouble()
                val stndWeight: Double
                val Fat: Double
                if (btn_boy.isChecked) {
                    stndWeight = (height - 80) * 0.7
                    Fat = (weight - 0.88 * stndWeight) / weight * 100
                } else {
                    stndWeight = (height - 70) * 0.6
                    Fat = (weight - 0.82 * stndWeight) / weight * 100
                }
                btn_calculate.isEnabled=true
                tv_weight.text = "標準體重\n${String.format("%.2f", stndWeight)}"
                tv_bmi.text = "體脂肪\n${String.format("%.2f", Fat)}"
            }

        }catch (e:Exception){
            Log.e(localClassName,"Cancel",e)
        }
    }
}