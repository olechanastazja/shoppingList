package com.example.shoppinglist2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist2.databinding.ActivityMainBinding


class MainActivity: AppCompatActivity() {
    private lateinit var sp: SharedPreferences
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.et1.text = intent.getStringExtra("user")
        sp = getSharedPreferences("options", Context.MODE_PRIVATE)

        binding.bt1.setOnClickListener {
            val intent1 = Intent(this, ProductListActivity::class.java)
            startActivity(intent1)
        }
        binding.bt2.setOnClickListener {
            val intent2 = Intent(this, OptionsActivity::class.java)
            startActivity(intent2)
        }
    }

    override fun onStart() {
        super.onStart()
        val fontSize = sp.getString("font_size","18" )
        val textColor =  sp.getInt("color", Color.GREEN)
        binding.et1.setTextSize(fontSize.toString().toFloat())
        binding.et1.setTextColor(textColor)
    }
}
