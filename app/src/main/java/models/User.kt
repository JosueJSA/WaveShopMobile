package models

data class User(
    val id:Int,
    val email:String,
    val userName:String,
    val password:String,
    val phone:String,
    val description:String,
    val status:String,
    val birthDay:String,
    val age:Int,
    val userType:String,
    val reputation:String,
    val lastLogin:String,
    val lastUpdate:String,
    val addresses:List<Address>,
    val favorites:List<Favorite>,
    val orders:List<Order>
)