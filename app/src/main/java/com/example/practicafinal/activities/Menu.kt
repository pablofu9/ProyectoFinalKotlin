package com.example.practicafinal.activities

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogComprar
import com.example.practicafinal.dialogs.DialogFiltro
import com.example.practicafinal.fragments.FragmentCarrito
import com.example.practicafinal.fragments.FragmentPerfil
import com.example.practicafinal.fragments.FragmentProductos
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener, FragmentPerfil.DataCallback,
    DialogComprar.MyDialogListener {
    private lateinit var topAppBar: MaterialToolbar
    val manager = supportFragmentManager
    val fragmentPerfil = FragmentPerfil()
    val fragmentProductos = FragmentProductos()
    val fragmentCarrito = FragmentCarrito()
    private lateinit var frame1: FrameLayout
    var userCambiado: User? = null
    var objetoComprado: Calzado? = null
    private var carritoLleno: Boolean = false
    var user: User = User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)
        frame1 = findViewById(R.id.frame1)
        //Cuando nos logueamos le ponemos el titulo al menu
        if (intent.hasExtra("user")) {
            /**
             * Para poner el titulo del menu, convertimos el nombre del usuario y ponemos la primera letra mayuscula
             */
            user = intent.getSerializableExtra("user") as User
            var title = user.name.toString()
            var titleUpper = title.replaceFirst(
                title[0].toString(), title[0].toString().toUpperCase(
                    Locale.ROOT
                )
            )
            topAppBar.title = titleUpper.toString()
        }
        //Para que el fragment donde salen los productos sea el que se cargue default

        onResume()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profile -> {
                /**
                 * Le mandamos al fragment de perfil, el usuario que hemos obtenido del login
                 */
                val transaction = manager.beginTransaction()
                val bundle = Bundle()
                if (userCambiado != null) {
                    bundle.putSerializable("user", userCambiado)
                } else {
                    bundle.putSerializable("user", user)
                }
                fragmentPerfil.arguments = bundle
                transaction.replace(R.id.frame1, fragmentPerfil)
                transaction.addToBackStack(null)
                transaction.commit()
                return true
            }
            R.id.inicio -> {
                onResume()
                //Nos saca un editext de buscar
                /*
                val dialogo = DialogFiltro()
                dialogo.show(supportFragmentManager,"Filtrar")*/
                return true
            }
            R.id.carrito->{
                val transaction = manager.beginTransaction()
                if(objetoComprado!=null){
                    var bundle = Bundle()
                    bundle.putSerializable("zapato", objetoComprado)
                    fragmentCarrito.arguments = bundle
                }
                Log.d("zz",objetoComprado.toString())
                transaction.replace(R.id.frame1, fragmentCarrito)
                transaction.addToBackStack(null)
                transaction.commit()
                return true
            }
            R.id.exit -> {
                //Para salir a la pantalla de login
                /**
                 * Cuando salimos, borramos las preferncias para que salga al login y no se quede guardado el user
                 */
                deleteSharedPreferences("my_user")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }

    override fun onBackPressed() {
        /**
         * CUando le damos para atras, recogemos el intent como un user y lo guardamos en preferencias
         * Las vamos a recoger en el main y las convertiremos en un user, para recargar otra vez la pantalla de menu
         */
        if (intent.hasExtra("user")) {
            var user = intent.getSerializableExtra("user") as User
            super.onBackPressed()

            val sharedPreferences = getSharedPreferences("my_user", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("id", user.id)
            editor.putString("name", user.name)
            editor.putString("pass", user.pass)
            editor.putString("email", user.email)
            editor.putBoolean("admin", user.admin)
            editor.apply()
        }


        // Exit the app
        finishAffinity()
    }

    //Aqui vamos a recibir el usuario, con la modificaion del email hecho
    override fun onDataReceived(u: User) {

        /**
         * Esto es para comprobar si este user no es null, si no lo es, es que hemos modificado el email.
         * Si hemos modificado el email, los argumentos que vamos a pasar van a ser distintos
         */
        userCambiado = u


    }

    //Mediante este metodo, recibimos la información del dialogo.
    override fun onDialogPositiveClick(z: Calzado) {
        /**
         * Preguntar
         */
        objetoComprado = z
        carritoLleno = true

        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frame1, fragmentProductos)
        transaction.addToBackStack(null)
        transaction.commit()
        /*
        YoYo.with(Techniques.Shake)
            .duration(800)
            .repeat(2)
            .playOn(findViewById(R.id.comprar));*/


        //El objeto esta inicilizado a null para asi ver si hemos comprado o no
    }

    override fun onResume() {
        super.onResume()
        val transaction = manager.beginTransaction()
        if (objetoComprado != null) {
            var bundle = Bundle()
            bundle.putSerializable("zapato", objetoComprado)
            fragmentProductos.arguments = bundle
        }
        transaction.replace(R.id.frame1, fragmentProductos)
        transaction.addToBackStack(null)
        transaction.commit()


    }


}