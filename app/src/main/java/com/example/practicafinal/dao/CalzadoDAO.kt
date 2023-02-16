package com.example.practicafinal.dao

import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import retrofit2.Call
import retrofit2.http.*

interface CalzadoDAO {
    @POST("calzados") //Debe coincidir con la ruta dle servlet
    fun createCalzado(@Body calzado: Calzado): Call<Calzado>

    // Método para obtener todas las películas
    @GET("calzados")
    fun getCalzados(): Call<List<Calzado>>

    // Método para obtener una película por su ID
    @GET("calzado/{id}")
    fun getCalzado(@Path("id") id: Int): Call<Calzado>

    @DELETE("calzado/{id}")
    fun deleteCalzado(@Path("id") id: Int): Call<Calzado>

}