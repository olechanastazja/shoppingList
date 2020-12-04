package com.example.shoppinglist2

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist2.MyAdapter
import com.example.shoppinglist2.Product
import com.example.shoppinglist2.ProductRepo
import com.example.shoppinglist2.ProductViewModel
import com.example.shoppinglist2.databinding.ActivityProductListBinding
//import kotlinx.android.synthetic.main.activity_main.*


class ProductListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent
        binding.textView.text = intent.getStringExtra("tv1text")

        binding.rv1.layoutManager = LinearLayoutManager(this) //LayoutManager
        binding.rv1.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        val productViewModel = ProductViewModel(application)
        val adapter = MyAdapter(productViewModel)
        binding.rv1.adapter = adapter
        productViewModel.allProducts.observe(this, Observer { products ->
            products.let{
                adapter.setProducts(it)
            }
        })
        binding.button.setOnClickListener {
            productViewModel.insert(
                Product(
                binding.etName.text.toString(),
                binding.etPrice.text.toString().toInt(),
                binding.etQuantity.text.toString().toInt(),
                binding.checkBox.isChecked)
            )
        }
        binding.button.setOnLongClickListener {
            productViewModel.deleteAll()
            true
        }
    }
}