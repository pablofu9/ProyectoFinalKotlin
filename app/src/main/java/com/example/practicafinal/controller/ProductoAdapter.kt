package com.example.practicafinal.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.practicafinal.R
import com.example.practicafinal.model.Calzado

class ProductoAdapter(private val mContext: Context, private val listaCalzado: List<Calzado>) : ArrayAdapter<Calzado>
    (mContext, R.layout.item_calzado,listaCalzado)
{
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater =LayoutInflater.from(mContext)
        val convertView =inflater.inflate(R.layout.item_calzado,null)
        val txtMarca = convertView?.findViewById<TextView>(R.id.txtMarca)
        val txtModelo = convertView?.findViewById<TextView>(R.id.txtModelo)
        val txtTalla = convertView?.findViewById<TextView>(R.id.txtTalla)
        val txtPrecio = convertView?.findViewById<TextView>(R.id.txtPrecio)

        txtMarca!!.text = listaCalzado[position].marca
        txtModelo!!.text = listaCalzado[position].modelo
        txtTalla!!.text = listaCalzado[position].talla.toString()
        txtPrecio!!.text = listaCalzado[position].precio.toString() + " € "
        return convertView
    }
}