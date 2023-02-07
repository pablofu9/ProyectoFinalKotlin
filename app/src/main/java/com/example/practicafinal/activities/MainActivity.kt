package com.example.practicafinal.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.practicafinal.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.muddz.styleabletoast.StyleableToast


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var editUser:TextInputEditText
    private lateinit var editPass:TextInputEditText
    private lateinit var txtCambio:TextView
    private lateinit var btnLogin:Button
    private lateinit var txtEmail:TextInputLayout
    private lateinit var btnVolver:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //StyleableToast.makeText(this, "Hola",R.style.ColoredBackground).show()
        editUser=findViewById(R.id.editUser)
        editPass=findViewById(R.id.editPass)
        txtCambio=findViewById(R.id.txtCambio)
        btnLogin=findViewById(R.id.btnLogin)
        txtEmail=findViewById(R.id.txtEmail)
        btnVolver=findViewById(R.id.btnVolver)
        txtCambio.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        btnVolver.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.txtCambio->{
                txtEmail.visibility=View.VISIBLE
                txtCambio.visibility=View.GONE
                btnLogin.setText("Sign up")
                btnVolver.visibility=View.VISIBLE
            }
            R.id.btnVolver->{
                txtEmail.visibility=View.GONE
                txtCambio.visibility=View.VISIBLE
                btnLogin.setText("Sign in")
                btnVolver.visibility=View.GONE
            }
            R.id.btnLogin-> {
                val intent = Intent(this, Menu::class.java)
                startActivity(intent)
            }
        }

    }

}