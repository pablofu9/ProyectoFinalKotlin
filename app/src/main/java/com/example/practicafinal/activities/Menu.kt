package com.example.practicafinal.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogFiltro
import com.google.android.material.appbar.MaterialToolbar

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private lateinit var topAppBar:MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar=findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)


    }


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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return true
            }
            else->return false
        }
    }


}