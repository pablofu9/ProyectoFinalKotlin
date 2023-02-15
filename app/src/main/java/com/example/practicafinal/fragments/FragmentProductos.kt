package com.example.practicafinal.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.lifecycle.MutableLiveData
import com.example.practicafinal.R
import com.example.practicafinal.adapter.ProductoAdapter
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import com.example.practicafinal.services.CalzadoService
import com.example.practicafinal.services.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentProductos : Fragment() {

    private lateinit var listaProductos:ListView
    private var arrayZapatillas = ArrayList<Calzado>()
    val calzadoService = CalzadoService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_productos, container, false)
        listaProductos=view.findViewById(R.id.listaProductos)
        arrayZapatillas = ArrayList<Calzado>()
        calzadoService.getCalzados().enqueue(object : Callback<List<Calzado>> {
            override fun onResponse(call: Call<List<Calzado>>, response: Response<List<Calzado>>) {
                if (response.isSuccessful) {
                    for (calzado in response.body()!!)
                        arrayZapatillas.add(calzado)
                    Log.d("zapas",arrayZapatillas.toString())

                    // Create the adapter and set it on the RecyclerView
                    /*val adapter = ProductoAdapter(requireContext(),arrayZapatillas)
                    listaProductos.adapter = adapter*/
                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<Calzado>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })

        return view
    }

    /*fun getCalzados() {
        calzadoService.getCalzados().enqueue(object : Callback<List<Calzado>> {
            override fun onResponse(call: Call<List<Calzado>>, response: Response<List<Calzado>>) {
                if (response.isSuccessful) {
                    for (calzado in response.body()!!)
                        arrayZapatillas.add(calzado)
                    Log.d("zapas",arrayZapatillas.toString())
                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<Calzado>>, t: Throwable) {
                // something went completel1y south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
    }*/


    /*override fun onStart() {
        super.onStart()
        getCalzados()
    }*/
}