package com.example.carrotmarket

data class User(
    var name:String,
    var email:String,
    var birth:String,
    var uid:String
){
    constructor(): this("","","","")
}
