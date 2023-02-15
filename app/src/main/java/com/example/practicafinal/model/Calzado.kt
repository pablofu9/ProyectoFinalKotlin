package com.example.practicafinal.model

import android.os.Parcelable

class Calzado:java.io.Serializable {
    val id_calzado:Int
    val marca:String
    val modelo:String
    val talla:Int
    val precio:Int

    constructor(id_calzado: Int, marca: String, modelo: String, talla: Int,precio:Int) {
        this.id_calzado = id_calzado
        this.marca = marca
        this.modelo = modelo
        this.talla = talla
        this.precio=precio
    }
    constructor( marca: String, modelo: String, talla: Int,precio:Int) {
        this.id_calzado = 0
        this.marca = marca
        this.modelo = modelo
        this.talla = talla
        this.precio=precio
    }
    constructor() {
        this.id_calzado = 0
        this.marca = ""
        this.modelo = ""
        this.talla = 0
        this.precio=0
    }


    override fun toString(): String {
        return "Calzado(id_calzado=$id_calzado, marca='$marca', modelo='$modelo', talla=$talla)"
    }


}