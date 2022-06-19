package models

data class ShoppingCart(

    val id: Int,
    val productsQuantity: Int,
    val subtotal: Double,
    val LastUpdate: String?,
    val IdUser: Int,

    val ProductSelectedCarts: List<ProductSelectedCart?>?

)
