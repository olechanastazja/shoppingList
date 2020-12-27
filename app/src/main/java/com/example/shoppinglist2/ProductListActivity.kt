package com.example.shoppinglist2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist2.databinding.ActivityProductListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.getCurrentUser()

        val database = FirebaseDatabase.getInstance()
        if (user != null) {
            val ref = database.getReference("users/" + user.uid)
            val productRef = ref.child("products")

            val intent = intent
            binding.textView.text = intent.getStringExtra("tv1text")

            binding.rv1.layoutManager = LinearLayoutManager(this) //LayoutManager
            binding.rv1.addItemDecoration(
                    DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            )
            val list = arrayListOf<Product>()
            val adapter = MyAdapter(this, list, productRef)
            binding.rv1.adapter = adapter
            binding.button.setOnClickListener {
                val id = productRef.push().getKey()!!
                    val product = Product(
                            id = id,
                            binding.etName.text.toString(),
                            binding.etPrice.text.toString().toInt(),
                            binding.etQuantity.text.toString().toInt(),
                            binding.checkBox.isChecked)

                productRef.child(id).setValue(product)
            }

            binding.button.setOnLongClickListener {
                productRef.setValue(null)
                true
            }
        }
    }

}