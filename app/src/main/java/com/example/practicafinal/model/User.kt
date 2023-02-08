package com.example.practicafinal.model

class User: java.io.Serializable {
    val id_calzado:Int
    val marca:String
    val modelo:String
    val talla:Int

    constructor(id_calzado: Int, marca: String, modelo: String, talla: Int) {
        this.id_calzado = id_calzado
        this.marca = marca
        this.modelo = modelo
        this.talla = talla
    }

    constructor( marca: String, modelo: String, talla: Int){
        this.id_calzado=0
        this.marca = marca
        this.modelo = modelo
        this.talla = talla
    }

    override fun toString(): String {
        return "User(id_calzado=$id_calzado, marca='$marca', modelo='$modelo', talla=$talla)"
    }


}