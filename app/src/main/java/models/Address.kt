package models

data class Address(
    val id:Int,
    val zip:String,
    val street:String,
    val state:String,
    val city:String,
    val idUser:Int
)
