package com.example.firestore.models

class User {

    var name: String? = null
    var age: String? = null
    var imgUrl: String? = null

    constructor()

    constructor(name: String?, age: String?, imgUrl: String?) {
        this.name = name
        this.age = age
        this.imgUrl = imgUrl
    }
}