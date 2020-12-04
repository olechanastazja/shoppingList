package com.example.shoppinglist2

import androidx.lifecycle.LiveData

class ProductRepo(private val productDao: ProductDao) {

    val allProduct: LiveData<List<Product>> = productDao.getProducts()

    fun insert(product: Product) = productDao.insert(product)

    fun update(product: Product) = productDao.update(product)

    fun delete(product: Product) = productDao.delete(product)

    fun deleteAll() = productDao.deleteAll()
}