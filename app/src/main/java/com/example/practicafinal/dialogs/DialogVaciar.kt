package com.example.practicafinal.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.fragments.FragmentCarrito
import com.example.practicafinal.model.Calzado
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogVaciar(z:Calzado): DialogFragment(),DialogInterface.OnClickListener {
    private var zapato:Calzado = z
    private lateinit var listener: DialogVaciar.MyDialogListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Vaciar el carrito")
        builder.setMessage("¿Seguro que quiere vaciar el carrito?")
        builder.setPositiveButton("Aceptar",this)
        builder.setNegativeButton("Cancelar",this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                Log.d("Zapato",zapato.toString())
                listener.onDialogPositiveClick2(zapato)
                //dismiss()
            }
            -2->{

            }
        }
    }

    interface MyDialogListener {
        fun onDialogPositiveClick2(z: Calzado)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as MyDialogListener
        }catch (e : java.lang.ClassCastException){
            throw ClassCastException("$context must implement MyDialogListener")

        }
    }


}