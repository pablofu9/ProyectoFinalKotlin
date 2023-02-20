package com.example.practicafinal.dialogs.dialogsUser

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.RangeSlider

class DialogFiltro: DialogFragment(), OnClickListener {

    private lateinit var spinnerMarcas:Spinner
    private lateinit var spinnerModelo:Spinner
    private lateinit var sliderTalla:RangeSlider

    //Dialogo para filtrar las zapatillas
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { MaterialAlertDialogBuilder(it) }

        val inflater=requireActivity().layoutInflater
        val view:View=inflater.inflate(R.layout.filtro,null)

        spinnerMarcas=view.findViewById(R.id.spinnerMarcas)
        spinnerModelo=view.findViewById(R.id.spinnerModelo)
        sliderTalla=view.findViewById(R.id.sliderTalla)
        val arraymarcas = arrayOf("Nike", "Adidas", "Puma", "Reebok")
        val modelo = arrayOf("1","2","3","4")

        val adapterMarcas = context?.let { ArrayAdapter(it, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,arraymarcas) }
        spinnerMarcas.adapter=adapterMarcas

        val adapterModelo = context?.let { ArrayAdapter(it, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,modelo) }
        spinnerModelo.adapter=adapterModelo
        builder?.setView(view)
        builder?.setPositiveButton("Aceptar", this)
        builder?.setNegativeButton("Salir",this)

        return builder?.create()!!

    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                //Boton de aceptar


            }
            -2->{
                //Boton de cancelar
                dialog?.dismiss()
            }
        }
    }


}