package com.example.practicafinal.controller

import android.content.Context
import android.widget.ArrayAdapter
import com.example.practicafinal.R
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User

class UsersAdapter(private val mContext: Context, private val listaUsers: List<User>) : ArrayAdapter<User>
    (mContext, R.layout.item_users,listaUsers) {
}