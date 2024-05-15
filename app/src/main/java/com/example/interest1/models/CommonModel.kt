package com.example.interest1.models

data class CommonModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty",
    var interest_fishing: Boolean = false,
    var interest_traveling: Boolean = false,
    var interest_swimming: Boolean = false,
    var text: String = "",
    var type: String = "",
    var from: String = "",
    var timeStamp: Any = "",
    var fileUrl:String = "empty",
    var lastmessage:String = "",
    var choice:Boolean = false




) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}