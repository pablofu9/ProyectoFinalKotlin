package com.example.practicafinal.model

class User: java.io.Serializable {
    val id:Int
    val name:String
    val pass:String
    val email:String
    val admin:Boolean

    constructor(id: Int, name: String, pass: String, email: String, admin: Boolean) {
        this.id = id
        this.name = name
        this.pass = pass
        this.email = email
        this.admin = admin
    }

    constructor( name: String, pass: String, email: String, admin: Boolean){
        this.id=0
        this.name = name
        this.pass = pass
        this.email = email
        this.admin = admin
    }
    constructor(){
        this.id=0
        this.name = ""
        this.pass = ""
        this.email = ""
        this.admin = false
    }

    override fun toString(): String {
        return "User(id=$id, name='$name', pass='$pass', email='$email', admin=$admin)"
    }


}