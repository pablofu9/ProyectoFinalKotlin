package com.example.practicafinal.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogAddCalzado
import com.example.practicafinal.dialogs.DialogComprar
import com.example.practicafinal.dialogs.DialogVaciar
import com.example.practicafinal.fragments.FragmentCarrito
import com.example.practicafinal.fragments.FragmentPerfil
import com.example.practicafinal.fragments.FragmentProductos
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentUsers
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentZapatillas
import com.example.practicafinal.interfaces.OnDeleteConfirmListener
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener, FragmentPerfil.DataCallback,
    DialogComprar.MyDialogListener,OnDeleteConfirmListener,DialogVaciar.MyDialogListener {

    private lateinit var topAppBar: MaterialToolbar
    val manager = supportFragmentManager
    val fragmentPerfil = FragmentPerfil()
    val fragmentProductos = FragmentProductos()
    val fragmentCarrito = FragmentCarrito()
    val fragmentUsers=FragmentUsers()
    val fragmentZapatillas = FragmentZapatillas()
    private lateinit var frame1: FrameLayout
    private lateinit var appBar:AppBarLayout
    var userCambiado: User? = null
    var objetoComprado: Calzado? = null
    private var carritoLleno: Boolean = false
    private lateinit var navigation:BottomNavigationView
    var user: User = User()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)
        frame1 = findViewById(R.id.frame1)
        navigation=findViewById(R.id.navigation)

        //Cuando nos logueamos le ponemos el titulo al menu
        if (intent.hasExtra("user")) {
            /**
             * Para poner el titulo del menu, convertimos el nombre del usuario y ponemos la primera letra mayuscula
             */
            //topAppBar.visibility=View.GONE
            //navigation.visibility=View.VISIBLE
            user = intent.getSerializableExtra("user") as User
            if(user.admin){
                topAppBar.visibility=View.GONE
                navigation.visibility=View.VISIBLE
                cargarUsersAdmin() //Si es admin, la pantalla principal seran los usuarios
                //Si el usuario es admin, se quita la barra que le aparecen a los usuarios y se pone la otra
                navigation.setOnItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.exit2 -> {
                            deleteSharedPreferences("my_user")
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            true
                        }
                       R.id.shoes -> {
                           cargarZapatillas()
                            // Handle tab 2 selection
                            true
                        }
                        R.id.users -> {
                            //Le mandamos el usuario

                            cargarUsersAdmin()
                            true
                        }
                        else -> false
                    }
                }

            }else{
                var title = user.name.toString()
                var titleUpper = title.replaceFirst(
                    title[0].toString(), title[0].toString().toUpperCase(
                        Locale.ROOT
                    )
                )
                topAppBar.title = titleUpper.toString()
                cargarPrincipal()
            }

        }
        //Para que el fragment donde salen los productos sea el que se cargue default
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
                cargarPrincipal()
                //Nos saca un editext de buscar
                /*
                val dialogo = DialogFiltro()
                dialogo.show(supportFragmentManager,"Filtrar")*/
                return true
            }
            R.id.carrito->{
                cargarFragmentCarrito()
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
         * Recogemos el boton de ok en el dialogo de comprar y mandamos el item comprado, para que asi
         * no salga el context menu habilitado porque ya hay un item en el carrito
         */
        objetoComprado = z
        carritoLleno = true
        Log.d("comprado",z.toString())
        val transaction2 = manager.beginTransaction()
        transaction2.remove(fragmentProductos)
        transaction2.addToBackStack(null)
        transaction2.commit()

        val transaction = manager.beginTransaction()
        var bundle = Bundle()
        bundle.putSerializable("comprado",objetoComprado)
        fragmentProductos.arguments = bundle
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

    fun cargarPrincipal() {

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

    fun reloadProductos(){
        val transaction = manager.beginTransaction()
        var bundle = Bundle()

        bundle.putSerializable("comprado", objetoComprado)
        fragmentProductos.arguments = bundle
        transaction.replace(R.id.frame1, fragmentProductos)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onDeleteConfirmed() {
        val transaction = manager.beginTransaction()
        var bundle = Bundle()
        transaction.remove(fragmentCarrito)
        transaction.commit()

    }
    override fun onDialogPositiveClick2(z: Calzado) {
        //Esto es lo que sucede cuando se cierra el dialogo de vaciar el carrito
        objetoComprado=null
        cargarFragmentCarrito()

        //cargarFragmentCarrito()
    }
    fun cargarFragmentCarrito(){
        val transaction = manager.beginTransaction()
        var bundle = Bundle()
        if(objetoComprado!=null) {
            bundle.putSerializable("zapato", objetoComprado)
        }
        if (userCambiado != null) {
            bundle.putSerializable("user", userCambiado)
        } else {
            bundle.putSerializable("user", user)
        }
            fragmentCarrito.arguments = bundle


        transaction.replace(R.id.frame1, fragmentCarrito)
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun cargarUsersAdmin(){
        val transaction = manager.beginTransaction()
        val bundle=Bundle()
        bundle.putSerializable("user",user)
        fragmentUsers.arguments=bundle
        transaction.replace(R.id.frame1, fragmentUsers)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun cargarZapatillas(){
        val transaction = manager.beginTransaction()

        transaction.replace(R.id.frame1, fragmentZapatillas)
        transaction.addToBackStack(null)
        transaction.commit()
    }



}