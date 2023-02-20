package com.example.practicafinal.fragments.fragmentsUser

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.practicafinal.R
import com.example.practicafinal.controller.FacturaAdapter
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.Factura
import com.example.practicafinal.model.User
import com.example.practicafinal.services.FacturaService
import com.example.practicafinal.services.UserService
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FragmentPerfil : Fragment(), View.OnClickListener {
    private lateinit var txtUser1: TextView
    private lateinit var txtPass1: TextView
    private lateinit var editEmail: EditText
    private lateinit var editCambioEmail: EditText
    private lateinit var btnCambio: Button
    private lateinit var btnAceptar: Button
    private lateinit var listaFacturas: ListView
    var user: User = User()
    val facturaService=FacturaService()
    private lateinit var dataCallback: DataCallback //Inicilizamos el datacallback
    var userCambiado: User = User()
    var emailCambiado: Boolean = false
    val serviceUser = UserService()
    var arrayFactura=ArrayList<Factura>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)
        txtUser1 = view.findViewById(R.id.txtUser1)
        txtPass1 = view.findViewById(R.id.txtPass1)
        editEmail = view.findViewById(R.id.editEmail)
        editCambioEmail = view.findViewById(R.id.editCambioEmail)
        btnCambio = view.findViewById(R.id.btnCambio)
        btnAceptar = view.findViewById(R.id.btnAceptar)
        listaFacturas = view.findViewById(R.id.listaFacturas)
        btnCambio.setOnClickListener(this)
        btnAceptar.setOnClickListener(this)
        /**
         * Hemos enviado desde la actividad menu, como argumentos a este fragment, el usuario que recogiamos del login
         * Desde aqui vamos a poder visualizar nuestro perfil, y ademas vamos a poder cambiar la direccion de email
         * Ponemos el email como no editable, hasta que le demos al boton para cambiar email, entonces se hara editable
         */
        if (arguments != null) {
            user = arguments?.getSerializable("user") as User
            editEmail.isEnabled = false
        }
        facturaService.getFacturas().enqueue(object : Callback<List<Factura>> {
            /**
             * Cogemos los datos de la api y metemos en una lista de zapatillas, y ahora ponemos el adaptador.
             */

            override fun onResponse(call: Call<List<Factura>>, response: Response<List<Factura>>) {
                if (response.isSuccessful) {
                    arrayFactura.clear()
                    for (factura in response.body()!!)
                        if(factura.id_user==user.id){
                            //recorremos las facturas y solo mostramos las del usuario del que estamos loggeados
                            arrayFactura.add(factura)
                        }


                    val adapter = context?.let { FacturaAdapter(it,arrayFactura) }
                    listaFacturas.adapter = adapter
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
        onResume()

        return view

    }

    /**
     * Falta que si cerramos la app despues de cambiar el email, ese email quede cambiado
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCambio -> {
                btnAceptar.visibility = View.VISIBLE
                btnCambio.visibility = View.GONE
                editCambioEmail.visibility = View.VISIBLE
            }
            R.id.btnAceptar -> {
                if (validarEmail(editCambioEmail.text.toString())) {
                    user =
                        User(user.id, user.name, user.pass, editCambioEmail.text.toString(), false)
                    updateUser(user)
                    StyleableToast.makeText(
                        requireContext(),
                        "Email cambiado con exito",
                        R.style.ColoredBackgroundGreen
                    ).show()
                    onResume()
                    emailCambiado = true
                    editCambioEmail.setText("")
                    dataCallback.onDataReceived(user)

                } else {
                    editCambioEmail.error = "El formato del email es incorrecto"
                    YoYo.with(Techniques.DropOut)
                        .duration(800)
                        .repeat(2)
                        .playOn(view?.findViewById(R.id.editCambioEmail));
                }

            }
        }
    }

    fun updateUser(u: User) {
        serviceUser.updateUser(u)
    }

    fun validarEmail(email: String): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[2a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailPattern.matches(email)
    }

    override fun onResume() {
        super.onResume()

        txtUser1.setText(user.name)
        txtPass1.setText(user.pass)
        editEmail.setText(user.email)
    }

    //Esto es para poder mandar el usuario a la actividad menú, asi podemos actualizar el email para cada vez que entremos
    interface DataCallback {
        fun onDataReceived(user: User)
        fun onDialogPositiveClick2(z: Calzado)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Check that the parent activity has implemented the callback interface
        if (context is DataCallback) {
            // Set the dataCallback variable to the parent activity
            dataCallback = context
        } else {
            throw RuntimeException("$context must implement DataCallback")
        }
    }


}