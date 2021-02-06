package com.example.shoppinglist2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist2.databinding.ActivityStoreListBinding
import com.example.shoppinglist2.databinding.StoreListElementBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class StoreAdapter(val context: Context, val storeList: ArrayList<Store>, val ref: DatabaseReference): RecyclerView.Adapter<StoreAdapter.MyViewHolder>() {

        init {
            ref.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val store = Store(
                            id = snapshot.child("id").value as String,
                            name = snapshot.child("name").value as String,
                            description = snapshot.child("description").value as String,
                            radius = snapshot.child("radius").value as String,
                            lat = snapshot.child("lat").value as String,
                            lng = snapshot.child("lng").value as String
                    )
                    storeList.add(store)
                    notifyDataSetChanged()
                }


                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val store = Store(
                            id = snapshot.child("id").value as String,
                            name = snapshot.child("name").value as String,
                            description = snapshot.child("description").value as String,
                            radius = snapshot.child("radius").value as String,
                            lat = snapshot.child("lat").value as String,
                            lng = snapshot.child("lng").value as String
                    )
                    storeList.remove(store)
                    storeList.add(store)
                    notifyDataSetChanged()
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val store = Store(
                            id = snapshot.child("id").value as String,
                            name = snapshot.child("name").value as String,
                            description = snapshot.child("description").value as String,
                            radius = snapshot.child("radius").value as String,
                            lat = snapshot.child("lat").value as String,
                            lng = snapshot.child("lng").value as String
                    )
                    storeList.remove(store)
                    notifyDataSetChanged()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Store Adapter", "Failed to delete value.", error.toException())
                }

            })
        }

    class MyViewHolder(val binding: StoreListElementBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StoreListElementBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.sId.text = storeList[position].name
        holder.binding.sName.text = storeList[position].name
        holder.binding.sDescription.text = storeList[position].description
        holder.binding.sRadius.text = storeList[position].radius
        holder.binding.sLat.text = storeList[position].lat
        holder.binding.sLng.text = storeList[position].lng
        holder.binding.root.setOnClickListener {
            ref.child(storeList[position].id).removeValue()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = storeList.size
}