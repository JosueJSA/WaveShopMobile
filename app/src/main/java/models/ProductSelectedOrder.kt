package models

data class ProductSelectedOrder (
    val id:Int,
    val price:Double,
    val quantity:Int,
    val status:String,
    val idProduct:Int,
    val idOrder:Int
)