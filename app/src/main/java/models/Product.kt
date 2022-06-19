package models

data class Product(

    val id: Int? ,
    val name: String?,
    val description: String?,
    val videoAddress: String? ,
    val stockQuantity: Int,
    val unitPrice: Double,
    val status: String? ,
    val published: String? ,
    val country: String? ,
    val location: String? ,
    val idCategory: Int?,
    val idVendor: Int? ,
    val likesNumber: Int?,
    val dislikesNumber: Int?,
    val shoppedTimes: Int?,
    val commentsNumber: Int?,
    val lastUpdate: String? ,
    val vendorUsername: String? ,

    val comments: List<Comment>? ,
    val favorites: List<Favorite>? ,
    val productSelectedCarts: List<ProductSelectedCart> ?,
    val productSelectedOrders: List<ProductSelectedOrder>? ,
    val product_Images: List<Product_Image> ,

)
