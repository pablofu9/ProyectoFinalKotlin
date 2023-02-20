package com.example.practicafinal.fragments.fragmentsAdmin

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ListView
import com.example.practicafinal.R
import com.example.practicafinal.controller.ProductoAdapter
import com.example.practicafinal.dialogs.dialogsAdmin.DialogAddCalzado
import com.example.practicafinal.dialogs.dialogsAdmin.DialogUpdateCalzado
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.Factura
import com.example.practicafinal.services.CalzadoService
import com.example.practicafinal.services.FacturaService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentZapatillas : Fragment(), View.OnClickListener {

    private lateinit var listaZapatillas: ListView
    private var listaZapatos = ArrayList<Calzado>()
    private val calzadoService = CalzadoService()
    var adapter:ProductoAdapter?=null
    var facturaService=FacturaService()
    var arrayFactura=ArrayList<Factura>()
    var noBorrar:Boolean=false
    private lateinit var btnAddZapatillas:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_zapatillas, container, false)
        listaZapatillas = view.findViewById(R.id.listaZapatillas)
        btnAddZapatillas=view.findViewById(R.id.btnAddZapatillas)
        btnAddZapatillas.setOnClickListener(this)
        leerCalzado()
        registerForContextMenu(listaZapatillas)
        leerFacturas()//LLenamos el arraylist de facturas para comparar
        return view
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = requireActivity().menuInflater
        inflater.inflate(R.menu.menuusers, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        var calzado: Calzado = listaZapatillas.adapter?.getItem(info.position) as Calzado
        when (item.itemId) {
            R.id.eliminar -> {
                /**
                 * Recorremos el arraylist de facturas y lo comparamos con el item seleccionado
                 * Para que no haya problemas de delete on cascade entre tablas
                 * Entonces si alguien ya genero una factura de ese producto, no se podra borrar
                 */
                noBorrar=false //La ponemos en false, paar que si el usuario primero le da a uno que no se puede,
                //La varible no se quede en true y se puedan borrar los demas
                for (factura in arrayFactura) {
                    if (factura.id_calzado == calzado.id_calzado) {
                        noBorrar = true
                    }
                }
                    if(noBorrar){
                        activity?.let {
                            StyleableToast.makeText(
                                it,
                                "No se puede borrar, ese producto ya tiene facturas generadas",
                                R.style.ColoredBoldText
                            ).show()
                        }
                    }else{
                        deleteCalzado(calzado)
                        listaZapatos.remove(calzado)
                        reloadCalzado()
                        activity?.let {
                            StyleableToast.makeText(
                                it,
                                "Calzado borrado",
                                R.style.ColoredBackgroundGreen
                            ).show()
                        }
                    }

                //Si no coincide borramos

            }
            R.id.modificar -> {
                showDialogUpdate(calzado)

            }


        }
        return super.onContextItemSelected(item)
    }
    fun deleteCalzado(c: Calzado) {
        calzadoService.deleteCalzado(c)
    }
    fun reloadCalzado(){
        adapter = context?.let { ProductoAdapter(it, listaZapatos) }
        listaZapatillas.adapter = adapter
    }
    fun leerCalzado(){
        calzadoService.getCalzados().enqueue(object : Callback<List<Calzado>> {
            /**
             * Cogemos los datos de la api y metemos en una lista de zapatillas, y ahora ponemos el adaptador.
             */
            override fun onResponse(call: Call<List<Calzado>>, response: Response<List<Calzado>>) {
                listaZapatos.clear()
                if (response.isSuccessful) {
                    for (calzado in response.body()!!)
                        listaZapatos.add(calzado)

                    reloadCalzado()
                    // Create the adapter and set it on the RecyclerView

                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<Calzado>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
    }

    fun leerFacturas(){
        facturaService.getFacturas().enqueue(object : Callback<List<Factura>> {
            /**
             * Cogemos los datos de la api y metemos en una lista de zapatillas, y ahora ponemos el adaptador.
             */

            override fun onResponse(call: Call<List<Factura>>, response: Response<List<Factura>>) {
                if (response.isSuccessful) {
                    arrayFactura.clear()
                    for (factura in response.body()!!)
                        arrayFactura.add(factura)
                            //recorremos las facturas y solo mostramos las del usuario del que estamos loggeados





                    // Create the adapter and set it on the RecyclerView
                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<Factura>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
    }
    fun showDialogAdd() {
        val add = DialogAddCalzado()
        activity?.let { add.show(it.supportFragmentManager, "add") }
    }
    fun showDialogUpdate(c:Calzado) {
        val add = DialogUpdateCalzado(c)
        activity?.let { add.show(it.supportFragmentManager, "add") }
    }

    override fun onClick(v: View?) {
        showDialogAdd()
    }

}