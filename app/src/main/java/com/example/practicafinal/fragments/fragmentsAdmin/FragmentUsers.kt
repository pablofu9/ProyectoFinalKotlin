package com.example.practicafinal.fragments.fragmentsAdmin

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import android.widget.ListView
import com.example.practicafinal.R
import com.example.practicafinal.controller.UsersAdapter
import com.example.practicafinal.dialogs.dialogsAdmin.DialogAddUser
import com.example.practicafinal.dialogs.dialogsAdmin.DialogUpdateUser
import com.example.practicafinal.model.Factura
import com.example.practicafinal.model.User
import com.example.practicafinal.services.FacturaService
import com.example.practicafinal.services.UserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentUsers : Fragment(), View.OnClickListener {
    private lateinit var listaUsers: ListView
    private var arrayUsers = ArrayList<User>()
    val userService = UserService()
    val facturaService = FacturaService()
    var user: User? = null
    var adapter: UsersAdapter? = null
    var existe:Boolean=false
    private lateinit var btnAddUsers: FloatingActionButton
    private var arrayFactura = ArrayList<Factura>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_users, container, false)
        listaUsers = view.findViewById(R.id.listaUsers)
        btnAddUsers = view.findViewById(R.id.btnAddUsers)
        btnAddUsers.setOnClickListener(this)
        leer()//Para llenar el arraylist de usuarios
        leerFacturas()//Para llenar los arraylist de factura
        registerForContextMenu(listaUsers)
        if (arguments != null) {
            user = arguments?.getSerializable("user") as User

        }
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
        var userBorrar: User = listaUsers.adapter?.getItem(info.position) as User
        when (item.itemId) {
            R.id.eliminar -> {
                existe=false
                for (factura in arrayFactura){
                    if(userBorrar.id == factura.id_user){
                        existe = true
                    }
                }
                //Borramos el usuario selected

                //Primero comprobamos que el usuario que vamos a borrar es diferente al que estamos logueados
                if (user?.id == userBorrar.id) {

                    activity?.let {
                        StyleableToast.makeText(
                            it,
                            "No se puede eliminar el usuario que esta conectado actualmente",
                            R.style.ColoredBoldText
                        ).show()
                    }
                } else if(existe) {
                    activity?.let {
                        StyleableToast.makeText(
                            it,
                            "No se puede borrar usuarios con facturas generadas",
                            R.style.ColoredBoldText
                        ).show()
                    }
                }else{
                    //Si no coincide borramos
                    deleteUser(userBorrar)
                    arrayUsers.remove(userBorrar)
                    reload()
                    activity?.let {
                        StyleableToast.makeText(
                            it,
                            "Usuario eliminado con exito",
                            R.style.ColoredBackgroundGreen
                        ).show()
                    }
                }


            }
            R.id.modificar -> {
                //Comprobamos que no se este modificando el usuario en uso
                if (user?.id == userBorrar.id) {
                    activity?.let {
                        StyleableToast.makeText(
                            it,
                            "El usuario actual no es modificable",
                            R.style.ColoredBoldText
                        ).show()
                    }
                }else{
                    showDialogUpdate(userBorrar)
                }

            }
        }

        return super.onContextItemSelected(item)
    }

    fun deleteUser(u: User) {
        userService.deleteUser(u)
    }

    fun reload() {
        adapter = context?.let { UsersAdapter(it, arrayUsers) }
        listaUsers.adapter = adapter
    }


    override fun onClick(v: View?) {
        showDialogAdd()
    }

    fun showDialogAdd() {
        val add = DialogAddUser()
        activity?.let { add.show(it.supportFragmentManager, "add") }
    }

    fun showDialogUpdate(u:User){
        val update = DialogUpdateUser(u)
        activity?.let { update.show(it.supportFragmentManager, "update") }
    }

    fun leer() {
        userService.getUsers().enqueue(object : Callback<List<User>> {

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                arrayUsers.clear()
                if (response.isSuccessful) {
                    for (user in response.body()!!)
                        arrayUsers.add(user)

                    reload()
                    // Create the adapter and set it on the RecyclerView

                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
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
}