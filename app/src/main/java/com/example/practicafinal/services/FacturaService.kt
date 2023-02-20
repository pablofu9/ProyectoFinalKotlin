package com.example.practicafinal.services

import android.util.Log
import com.example.practicafinal.dao.CalzadoDAO
import com.example.practicafinal.dao.FacturaDAO
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.Factura
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FacturaService {
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.40:8080/ServidorREST/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun createFactura(factura: Factura) {
        getRetrofit().create(FacturaDAO::class.java).createFactura(factura).enqueue(object :
            Callback<Factura> {
            override fun onResponse(call: Call<Factura>, response: Response<Factura>) {
                Log.d("TAG", "CALL: $call")
                Log.d("TAG", "RESPONSE: $response")
            }
            override fun onFailure(call: Call<Factura>, t: Throwable) {
                // Procesar error en la petición
                Log.d("TAG", "Error")
            }
        })
    }

    //Método para ver todos los libros
    fun getFacturas() : Call<List<Factura>> {
        return getRetrofit().create(FacturaDAO::class.java).getFacturas()
    }
}