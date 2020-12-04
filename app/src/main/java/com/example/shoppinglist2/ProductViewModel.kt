package com.example.shoppinglist2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: ProductRepo
    val allProducts: LiveData<List<Product>>

    init {
        repo = ProductRepo(ProductDB.getDatabase(app).productDao())
        allProducts = repo.allProduct
    }

    fun insert(product: Product) = repo.insert(product)

    fun update(product: Product) = repo.update(product)

    fun delete(product: Product) = repo.delete(product)

    fun deleteAll() = repo.deleteAll()
}