package com.example.shoppinglist2

import android.R.id
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist2.databinding.ListElementBinding
import com.google.firebase.database.*


class MyAdapter(val context: Context, val productList: ArrayList<Product>, val ref: DatabaseReference) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    init {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val product = Product(
                        id = snapshot.child("id").value as String,
                        name = snapshot.child("name").value as String,
                        price = (snapshot.child("price").value as Long).toInt(),
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                )
                productList.add(product)
                notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val product = Product(
                        id = snapshot.child("id").value as String,
                        name = snapshot.child("name").value as String,
                        price = (snapshot.child("price").value as Long).toInt(),
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                )
                productList.remove(product)
                productList.add(product)
                notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val product = Product(
                        id = snapshot.child("id").value as String,
                        name = snapshot.child("name").value as String,
                        price = (snapshot.child("price").value as Long).toInt(),
                        quantity = (snapshot.child("quantity").value as Long).toInt(),
                        bought = snapshot.child("bought").value as Boolean
                )
                productList.remove(product)
                notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("My Adapter", "Failed to delete value.", error.toException())
            }

        })
    }
    class MyViewHolder(val binding: ListElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListElementBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvId.text = (position + 1).toString()
        holder.binding.tvName.text = productList[position].name
        holder.binding.tvPrice.text = productList[position].price.toString()
        holder.binding.tvQuantity.text = productList[position].quantity.toString()
        holder.binding.cbBought.isChecked = productList[position].bought
        holder.binding.tvName.setOnClickListener {
           ref.child( productList[position].id).removeValue()
        }
        holder.binding.cbBought.setOnClickListener {
            productList[position].bought = holder.binding.cbBought.isChecked
            val product = Product(
                    id = productList[position].id,
                    name=productList[position].name,
                    price=productList[position].price,
                    quantity=productList[position].quantity,
                    bought = holder.binding.cbBought.isChecked)
            ref.child(productList[position].id).setValue(product)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = productList.size


}