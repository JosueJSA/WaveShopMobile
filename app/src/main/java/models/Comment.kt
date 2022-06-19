package models

data class Comment(

    val id: Int,
    val userName: String?,
    val opinionResume: String?,
    val content: String?,
    val visible: String?,
    val photoAddress: String?,
    val likes: Int,
    val dislikes: Int,
    val published: String?,
    val idProduct: Int,
    val idComment: Int,

)
