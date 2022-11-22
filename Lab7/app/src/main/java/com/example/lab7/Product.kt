package com.example.lab7

class Product {

    private var id: Int = 0
    var productName: String? = null
    var quantity: Int = 0

    constructor(id: Int, productName: String, quantity: Int) {
        this.id = id
        this.productName = productName
        this.quantity = quantity
    }

    constructor(productName: String, quantity: Int) {
        this.productName = productName
        this.quantity = quantity
    }

    override fun toString(): String {
        return "Nazwa: $productName  Ilość: $quantity"
    }


}