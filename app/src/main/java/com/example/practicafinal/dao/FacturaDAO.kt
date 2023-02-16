package com.example.practicafinal.dao

import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.Factura
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FacturaDAO {
    @POST("facturas") //Debe coincidir con la ruta dle servlet
    fun createFactura(@Body factura: Factura): Call<Factura>

    @GET("facturas")
    fun getFacturas(): Call<List<Factura>>
}