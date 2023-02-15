package com.example.practicafinal.services

import android.util.Log
import com.example.practicafinal.dao.UserDAO
import com.example.practicafinal.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserService {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.145.107:8080/ServidorREST/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun createUser(user: User) {
        getRetrofit().create(UserDAO::class.java).createUser(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d("TAG", "CALL: $call")
                Log.d("TAG", "RESPONSE: $response")
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // Procesar error en la petición
                Log.d("TAG", "Error")
            }
        })
    }

    //Método para ver todos los libros
    fun getUsers(): Call<List<User>> {
        return getRetrofit().create(UserDAO::class.java).getUsers()
    }

    fun updateUser(user: User) {
        getRetrofit().create(UserDAO::class.java).updateUser(user.id,user)
            .enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Log.d("TAG", "CALL: $call")
                    Log.d("TAG", "RESPONSE: $response")
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Procesar error en la petición
                    Log.d("TAG", "Error")
                }
            })

    }
}