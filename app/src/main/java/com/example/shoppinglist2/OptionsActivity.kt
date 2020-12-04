package com.example.shoppinglist2

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglist2.databinding.ActivityOptionsBinding


class OptionsActivity : AppCompatActivity() {
    private lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sp = getSharedPreferences("options", Context.MODE_PRIVATE)
        val rg = binding.radioGroup
        val colorsMap = mapOf("green" to Color.GREEN, "red" to Color.RED)


        binding.button.setOnClickListener() {
            val selectedId = rg.checkedRadioButtonId
            val btn = findViewById<View>(selectedId) as RadioButton
            Log.i(getString(R.string.app_name), btn.text.toString())
            val editor = sp.edit()
            editor.putString("font_size", binding.etTextSize.text.toString())
            colorsMap[btn.text.toString()]?.let { it1 -> editor.putInt("color", it1) }
            editor.apply()
            finish();
        }
    }
}