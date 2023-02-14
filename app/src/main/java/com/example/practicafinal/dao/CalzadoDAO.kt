package com.example.practicafinal.dao

import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CalzadoDAO {
    @POST("calzados") //Debe coincidir con la ruta dle servlet
    fun createCalzado(@Body calzado: Calzado): Call<Calzado>

    // Método para obtener todas las películas
    @GET("calzados")
    fun getCalzados(): Call<List<Calzado>>

    // Método para obtener una película por su ID
    @GET("calzado/{id}")
    fun getCalzado(@Path("id") id: Int): Call<Calzado>
}