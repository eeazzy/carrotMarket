package com.example.carrotmarket

data class User(
    var name:String,
    var email:String,
    var phoneNum:String,
    var uid:String
){
    constructor(): this("","","","")
}
