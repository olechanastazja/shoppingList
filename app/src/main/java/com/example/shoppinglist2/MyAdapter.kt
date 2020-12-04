package com.example.shoppinglist2

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist2.ProductViewModel
import com.example.shoppinglist2.databinding.ListElementBinding

class MyAdapter(val productViewModel: ProductViewModel) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var productList = emptyList<Product>()

    class MyViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListElementBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvId.text = productList[position].id.toString()
        holder.binding.tvName.text = productList[position].name
        holder.binding.tvPrice.text = productList[position].price.toString()
        holder.binding.tvQuantity.text = productList[position].quantity.toString()
        holder.binding.cbBought.isChecked = productList[position].bought
        holder.binding.root.setOnClickListener {
            productViewModel.delete(productList[position])
            notifyDataSetChanged()
        }
        holder.binding.cbBought.setOnClickListener {
            productList[position].bought = holder.binding.cbBought.isChecked
            productViewModel.update(productList[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = productList.size

    fun setProducts(products: List<Product>){
        this.productList = products
        notifyDataSetChanged()
    }

}