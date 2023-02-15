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
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogFiltro
import com.example.practicafinal.fragments.FragmentPerfil
import com.example.practicafinal.fragments.FragmentProductos
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener,FragmentPerfil.DataCallback {
    private lateinit var topAppBar:MaterialToolbar
    val manager = supportFragmentManager
    val fragmentPerfil = FragmentPerfil()
    val fragmentProductos = FragmentProductos()
    private lateinit var frame1:FrameLayout
    var user:User=User()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar=findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)
        frame1 = findViewById(R.id.frame1)
        //Cuando nos logueamos le ponemos el titulo al menu
        if(intent.hasExtra("user")){
            /**
             * Para poner el titulo del menu, convertimos el nombre del usuario y ponemos la primera letra mayuscula
             */
            user = intent.getSerializableExtra("user") as User
            var title = user.name.toString()
            var titleUpper = title.replaceFirst(title[0].toString(), title[0].toString().toUpperCase(
                Locale.ROOT))
            topAppBar.title=titleUpper.toString()
        }
        //Para que el fragment donde salen los productos sea el que se cargue default
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frame1, fragmentProductos)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.profile->{
                /**
                 * Le mandamos al fragment de perfil, el usuario que hemos obtenido del login
                 */
                val transaction = manager.beginTransaction()
                val bundle = Bundle()
                bundle.putSerializable("user",user)
                fragmentPerfil.arguments=bundle
                transaction.replace(R.id.frame1, fragmentPerfil)
                transaction.addToBackStack(null)
                transaction.commit()
               return true
            }
            R.id.inicio->{
                val transaction = manager.beginTransaction()
                transaction.replace(R.id.frame1, fragmentProductos)
                transaction.addToBackStack(null)
                transaction.commit()
                //Nos saca un editext de buscar
                /*
                val dialogo = DialogFiltro()
                dialogo.show(supportFragmentManager,"Filtrar")*/
                return true
            }
            R.id.exit->{
                //Para salir a la pantalla de login
                /**
                 * Cuando salimos, borramos las preferncias para que salga al login y no se quede guardado el user
                 */
                deleteSharedPreferences("my_user")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else->return false
        }
    }

    override fun onBackPressed() {
        /**
         * CUando le damos para atras, recogemos el intent como un user y lo guardamos en preferencias
         * Las vamos a recoger en el main y las convertiremos en un user, para recargar otra vez la pantalla de menu
         */
        if(intent.hasExtra("user")){
            var user = intent.getSerializableExtra("user") as User
            super.onBackPressed()

            val sharedPreferences = getSharedPreferences("my_user", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt("id",user.id)
            editor.putString("name", user.name)
            editor.putString("pass",user.pass)
            editor.putString("email",user.email)
            editor.putBoolean("admin",user.admin)
            editor.apply()
        }


        // Exit the app
        finishAffinity()
    }

    override fun onDataReceived(data: Any) {
        val childActivity = FragmentPerfil()
        childActivity.setDataCallback(this)
        user = data as User

    }



}