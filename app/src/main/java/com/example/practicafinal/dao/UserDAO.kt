package com.example.practicafinal.dao

import com.example.practicafinal.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserDAO {

    @POST("users") //Debe coincidir con la ruta dle servlet
    fun createUser(@Body user: User): Call<User>

    // Método para obtener todas las películas
    @GET("users")
    fun getUsers(): Call<List<User>>

    // Método para obtener una película por su ID
    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>
}