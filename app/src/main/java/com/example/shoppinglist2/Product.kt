package com.example.shoppinglist2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(var id: String, var name: String, var price: Int, var quantity: Int, var bought: Boolean){
}