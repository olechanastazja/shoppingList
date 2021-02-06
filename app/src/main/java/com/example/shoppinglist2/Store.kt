package com.example.shoppinglist2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Store(var id: String,var name: String, var description: String, var radius: String,
                 var lat: String, var lng: String){
}