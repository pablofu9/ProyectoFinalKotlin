package com.example.practicafinal.model

class Factura:java.io.Serializable {
    val id_factura:Int
    val id_user:Int
    val id_calzado:Int
    val cantidad:Int
    val total:Int

    constructor(id_factura: Int, id_user: Int, id_calzado: Int, cantidad: Int, total: Int) {
        this.id_factura = id_factura
        this.id_user = id_user
        this.id_calzado = id_calzado
        this.cantidad = cantidad
        this.total = total
    }
    constructor( id_user: Int, id_calzado: Int, cantidad: Int, total: Int) {
        this.id_factura = 0
        this.id_user = id_user
        this.id_calzado = id_calzado
        this.cantidad = cantidad
        this.total = total
    }

    override fun toString(): String {
        return "Factura(id_factura=$id_factura, id_user=$id_user, id_calzado=$id_calzado, cantidad=$cantidad, total=$total)"
    }


}