package models

data class Order(
    val id:Int,
    val idUser:Int,
    val idShoppingCart:Int,
    val ordered:String,
    val shipped:String,
    val status:String,
    val total:Double,

    val productSelectedOrders:List<ProductSelectedOrder>
)
