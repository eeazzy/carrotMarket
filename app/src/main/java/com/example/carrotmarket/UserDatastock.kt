package com.example.carrotmarket

data class UserDatastock(//판매글에 대한 정보와 판매 유저의 정보
    var itemUid:String,
    var uid:String,//판매자의 uid
    var sellerName:String,//판매자의 이름
    var title:String,//제목
    var detail:String,//내용
    var price: Double,//가격
    var isSell:Boolean//판매여부
){
    constructor(): this("","","","","",0.0,false)//초기값
}
