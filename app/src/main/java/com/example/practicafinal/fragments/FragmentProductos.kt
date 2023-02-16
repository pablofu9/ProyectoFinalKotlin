package com.example.practicafinal.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.fragment.app.Fragment
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.controller.ProductoAdapter
import com.example.practicafinal.dialogs.DialogComprar
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import com.example.practicafinal.services.CalzadoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FragmentProductos : Fragment(),DialogComprar.OnDialogDismissListener  {

    private lateinit var listaProductos:ListView
    private var arrayZapatillas = ArrayList<Calzado>()
    val calzadoService = CalzadoService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_productos, container, false)
        Log.d("estamos","estamos")
        listaProductos=view.findViewById(R.id.listaProductos)
        arrayZapatillas = ArrayList<Calzado>()
        calzadoService.getCalzados().enqueue(object : Callback<List<Calzado>> {
            /**
             * Cogemos los datos de la api y metemos en una lista de zapatillas, y ahora ponemos el adaptador.
             */

            override fun onResponse(call: Call<List<Calzado>>, response: Response<List<Calzado>>) {
                if (response.isSuccessful) {
                    for (calzado in response.body()!!)
                        arrayZapatillas.add(calzado)
                    Log.d("zapas",arrayZapatillas.toString())
                    val adapter = context?.let { ProductoAdapter(it,arrayZapatillas) }
                    listaProductos.adapter = adapter
                    // Create the adapter and set it on the RecyclerView
                    if(arguments!=null){
                    }else{
                        registerForContextMenu(listaProductos)
                    }
                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<Calzado>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
        //Si nos llega de vuelta del ciclo fragment-menu-fragment, un argumento, es que hay un objeto en el carrito
        //Por lo tanto no se podran comprar mas



        return view
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?)
    {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater =requireActivity().menuInflater
        inflater.inflate(R.menu.menucomprar,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info =item.menuInfo as AdapterContextMenuInfo
        when(item.itemId){
            R.id.comprar->{
                //Creamos un dialogo de confirmación al que le mandamos el item como un objeto ca
                val zapato:Calzado=listaProductos.adapter?.getItem(info.position) as Calzado
                showDialog(zapato)
            }
        }
        return super.onContextItemSelected(item)
    }







    fun showDialog(c:Calzado){
        val comprar = DialogComprar(c)
        activity?.let { comprar.show(it.supportFragmentManager, "comprar") }
    }
    override fun onDismiss() {
        (activity as com.example.practicafinal.activities.Menu).reloadProductos()
    }
}