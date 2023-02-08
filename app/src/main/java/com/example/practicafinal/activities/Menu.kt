package com.example.practicafinal.activities

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogFiltro
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Menu : AppCompatActivity(), Toolbar.OnMenuItemClickListener {
    private lateinit var topAppBar:MaterialToolbar
    val serviceUser = UserService()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        topAppBar=findViewById(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener(this)


    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.profile->{
                getUsers()
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
    fun getUsers() {
        serviceUser.getUsers().enqueue(object: Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful)
                {
                    for (user in response.body()!!)
                        Log.d("TAG", user.toString())
                } else
                {
                    Log.d("TAG", "Error")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // something went completely south (like no internet connection)
                t.message?.let { Log.d("Error", it) }
            }
        })
    }


}