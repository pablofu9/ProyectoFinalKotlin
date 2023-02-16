package com.example.practicafinal.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.practicafinal.R
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User

class UsersAdapter(private val mContext: Context, private val listaUsers: List<User>) : ArrayAdapter<User>
    (mContext, R.layout.item_users,listaUsers) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val convertView =inflater.inflate(R.layout.item_users,null)
        val txtNombre = convertView.findViewById<TextView>(R.id.txtNombre)
        val txtPass = convertView.findViewById<TextView>(R.id.txtPass)
        val txtEmail = convertView.findViewById<TextView>(R.id.txtEmail)

        txtNombre.text=listaUsers[position].name
        txtPass.text=listaUsers[position].pass
        txtEmail.text=listaUsers[position].email

        return convertView
    }
}