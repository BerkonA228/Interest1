package com.example.interest1.models

data class UserModel(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "empty",
    var interest_fishing: Boolean = false,
    var interest_traveling: Boolean = false,
    var interest_swimming: Boolean = false
)