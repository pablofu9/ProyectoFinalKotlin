package com.example.practicafinal.controller

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.practicafinal.R
import com.example.practicafinal.model.Factura

class FacturaAdapter(private val mContext: Context, private val listaFactura: List<Factura>) : ArrayAdapter<Factura>
    (mContext, R.layout.item_calzado,listaFactura)
{
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mContext)
        val convertView =inflater.inflate(R.layout.item_facturas,null)
        val txtIdCalzado = convertView?.findViewById<TextView>(R.id.txtIdCalzado)
        val txtCantidadFactura = convertView?.findViewById<TextView>(R.id.txtCantidadFactura)
        val txtTotalFactura = convertView?.findViewById<TextView>(R.id.txtTotalFactura)


        txtIdCalzado!!.text = listaFactura[position].id_calzado.toString()
        txtCantidadFactura!!.text = listaFactura[position].cantidad.toString()
        txtTotalFactura!!.text = listaFactura[position].total.toString() + " € "

        return convertView
    }

}