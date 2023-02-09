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
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogFiltro
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private lateinit var topAppBar:MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar=findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)

        //Cuando nos logueamos le ponemos el titulo al menu
        if(intent.hasExtra("user")){
            /**
             * Para poner el titulo del menu, convertimos el nombre del usuario y ponemos la primera letra mayuscula
             */
            var user = intent.getSerializableExtra("user") as User
            var title = user.name.toString()
            var titleUpper = title.replaceFirst(title[0].toString(), title[0].toString().toUpperCase(
                Locale.ROOT))
            topAppBar.title=titleUpper.toString()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.profile->{
               return true
            }
            R.id.search->{
                //Nos saca un editext de buscar
                val dialogo = DialogFiltro()
                dialogo.show(supportFragmentManager,"Filtrar")
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




}