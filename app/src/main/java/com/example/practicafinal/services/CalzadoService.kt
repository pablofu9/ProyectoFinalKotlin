package com.example.practicafinal.services

import android.util.Log
import com.example.practicafinal.dao.CalzadoDAO
import com.example.practicafinal.dao.UserDAO
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CalzadoService {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.145.107:8080/ServidorREST/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun createCalzado(calzado: Calzado) {
        getRetrofit().create(CalzadoDAO::class.java).createCalzado(calzado).enqueue(object : Callback<Calzado> {
            override fun onResponse(call: Call<Calzado>, response: Response<Calzado>) {
                Log.d("TAG", "CALL: $call")
                Log.d("TAG", "RESPONSE: $response")
            }
            override fun onFailure(call: Call<Calzado>, t: Throwable) {
                // Procesar error en la petición
                Log.d("TAG", "Error")
            }
        })
    }

    //Método para ver todos los libros
    fun getCalzados() : Call<List<Calzado>> {
        return getRetrofit().create(CalzadoDAO::class.java).getCalzados()
    }
    fun deleteCalzado(c:Calzado){
        getRetrofit().create(CalzadoDAO::class.java).deleteCalzado(c.id_calzado)
            .enqueue(object : Callback<Calzado> {
                override fun onResponse(call: Call<Calzado>, response: Response<Calzado>) {
                    Log.d("TAG", "CALL: $call")
                    Log.d("TAG", "RESPONSE: $response")
                }

                override fun onFailure(call: Call<Calzado>, t: Throwable) {
                    // Procesar error en la petición
                    Log.d("TAG", "Error")
                }
            })
    }
    fun updateCalzado(c: Calzado) {
        getRetrofit().create(CalzadoDAO::class.java).updateCalzado(c.id_calzado,c)
            .enqueue(object : Callback<Calzado> {
                override fun onResponse(call: Call<Calzado>, response: Response<Calzado>) {
                    Log.d("TAG", "CALL: $call")
                    Log.d("TAG", "RESPONSE: $response")
                }

                override fun onFailure(call: Call<Calzado>, t: Throwable) {
                    // Procesar error en la petición
                    Log.d("TAG", "Error")
                }
            })

    }
}