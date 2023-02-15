package com.example.practicafinal.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.practicafinal.R
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.muddz.styleabletoast.StyleableToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var editUser: TextInputEditText
    private lateinit var editPass: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var txtCambio: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtEmail: TextInputLayout
    private lateinit var btnVolver: Button
    val serviceUser = UserService()
    private var arrayUsers = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //StyleableToast.makeText(this, "Hola",R.style.ColoredBackground).show()
        editUser = findViewById(R.id.editUser)
        editPass = findViewById(R.id.editPass)
        editEmail = findViewById(R.id.editEmail)
        txtCambio = findViewById(R.id.txtCambio)
        btnLogin = findViewById(R.id.btnLogin)
        txtEmail = findViewById(R.id.txtEmail)
        btnVolver = findViewById(R.id.btnVolver)
        txtCambio.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        btnVolver.setOnClickListener(this)
        arrayUsers = ArrayList<User>()
        /**
         * 1. Nos logeamos
         * 2. Mediante un intent le enviamos la informacion del user logeado a la actividad menu
         * 3. Despues con el user logeado si volvemos para atras guardamos un shared preferences que vemos en la actividad menu
         * 4. Asi cuando volvamos a iniciar la app la informacion del login estara guardada
         * 5. Solo si le damos a sign out saldremos al login de nuevo y habra que logearse de nuevo
         */
        val id: Int
        val name: String
        val pass: String
        val email: String
        val admin: Boolean
        val sharedPreferences = getSharedPreferences("my_user", Context.MODE_PRIVATE)
        if (sharedPreferences.contains("id")) {
            id = sharedPreferences.getInt("id", 0)
            name = sharedPreferences.getString("name", "").toString()
            pass = sharedPreferences.getString("pass", "").toString()
            email = sharedPreferences.getString("email", "").toString()
            admin = sharedPreferences.getBoolean("admin", false)

            var usuarioGuardado: User = User(id, name, pass, email, admin)
            val intent = Intent(this, Menu::class.java)
            intent.putExtra("user", usuarioGuardado)
            startActivity(intent)
        }

        //Hacemos la llamada al metodo, para llenar el arraylist con los usuarios que tenemos en la api
        getUsers()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.txtCambio -> {
                txtEmail.visibility = View.VISIBLE
                txtCambio.visibility = View.GONE
                btnLogin.setText("Sign up")
                btnVolver.visibility = View.VISIBLE
            }
            R.id.btnVolver -> {
                txtEmail.visibility = View.GONE
                txtCambio.visibility = View.VISIBLE
                btnLogin.setText("Sign in")
                btnVolver.visibility = View.GONE
            }
            R.id.btnLogin -> {
                /**
                 * Hay que comprobar esto del login, nos saca los toast varias veces al recorrer el for
                 */
                if (btnLogin.text.toString() == "Sign in") {
                    //Controlamos lo que pone en el boton para ver si hay que logearse o registrarse
                    if (!camposLoginVacios()) {
                        //Si los campos estan vacios te saca un toast para que rellenes los campos
                        StyleableToast.makeText(
                            this,
                            "Introduce tus credenciales",
                            R.style.ColoredBackground
                        ).show()
                    } else {
                        var correcto:Boolean=false
                        var userLogged:User=User()
                        for (user in arrayUsers) {
                            if (editUser.text.toString() == user.name && editPass.text.toString() == user.pass) {
                                //Usuario y pass correctos, vamos al menu, estamos logeados
                                correcto=true
                                userLogged=user
                                break
                            } else {
                                //Sale el toast 3 veces porque estamos dentro del for
                                //El usuario no es correcta
                                correcto=false
                            }
                        }
                        if(correcto){
                            val intent = Intent(this, Menu::class.java)
                            intent.putExtra("user", userLogged)
                            startActivity(intent)
                        }else{

                            editUser.error="Credenciales incorrectas"
                        }

                    }
                } else {
                    /**
                     * A partir de aqui es donde hacemos las comprobaciones de register
                     * 1.Comprobamos que no haya ningun usuario en la BD con ese nombre
                     * 2. Si no lo hay comprobamos nombre --> Entre 3 y 10 caracteres
                     * 3. Si lo anterior es correcto, comprobamos password seguro --> Entre 5 y 12
                     * 4. Mediante un metodo para ver el patron del email, comprobamos email correcto
                     * 5. Si todas las condiciones se cumplen, el usuario se registrara
                     */
                    var existe:Boolean=false
                    //Aqui nos registramos
                    if (!camposRegisterVacios()) {
                        //Si hay algun campo vacio no te deja avanzar
                        StyleableToast.makeText(
                            this, "Debes rellenar todos los " +
                                    "campos para registrarte", R.style.ColoredBackground
                        ).show()
                    } else {
                        for (user in arrayUsers){
                            if (editUser.text.toString() == user.name){
                                existe = true
                                break
                            }else{
                                existe = false
                            }
                        }
                        if(existe){
                            editUser.error="El usuario ya existe, pruebe con otro"
                        }else{
                            if (editUser.text!!.length <=3 || editUser.text!!.length >10 ){
                                editUser.error="Formato de usuario incorrecto (entre 3 y 10 caracteres)"
                            } else{
                                if (editPass.text!!.length <5 || editUser.text!!.length >12 ){
                                    editPass.error="Contraseña poco segura (Entre 5 y 12)"
                                }else{
                                    if(validarEmail(editEmail.text.toString())){
                                        StyleableToast.makeText(
                                            this,
                                            "Usuario registrado, inicie sesión",
                                            R.style.ColoredBackgroundGreen
                                        ).show()
                                        //Registro correcto
                                        txtEmail.visibility=GONE
                                        txtCambio.visibility=View.VISIBLE
                                        btnVolver.visibility=View.GONE
                                        btnLogin.setText("Sign in")
                                        val userRegister:User=User(editUser.text.toString(), editPass.text.toString(),
                                            editEmail.text.toString(), false)
                                        createUser(userRegister)
                                        arrayUsers.add(userRegister)

                                    }else{
                                        editEmail.error="Formato de email incorrecto"
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }

    }

    //Metemos los usuarios en un arraylist para comprobar el login
    fun getUsers() {
        serviceUser.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    for (user in response.body()!!)
                        arrayUsers.add(user)
                } else {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
    }
    fun createUser(u:User){
        serviceUser.createUser(u)
    }

    fun camposLoginVacios(): Boolean {
        return !(editUser.text.toString().isEmpty() || editPass.text.toString().isEmpty())
    }

    fun camposRegisterVacios(): Boolean {
        return !(editUser.text.toString().isEmpty() || editPass.text.toString()
            .isEmpty() || editEmail.text.toString().isEmpty())
    }

    fun validarEmail(email:String):Boolean{
        val emailPattern = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailPattern.matches(email)
    }
    //Para que desde el main, al darle para atras cierre la app
    override fun onBackPressed() {
        finishAffinity()
    }


}
