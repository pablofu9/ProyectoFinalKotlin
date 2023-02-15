package com.example.practicafinal.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.model.Calzado
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.muddz.styleabletoast.StyleableToast

//Alert dialog con material design
class DialogComprar(zapato:Calzado): DialogFragment(),DialogInterface.OnClickListener {
    private var z:Calzado = zapato
    private lateinit var listener: MyDialogListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("¿Añadir al carrito?")
        builder.setMessage(z.marca + " - "+z.modelo +"\n " +"Talla - "+z.talla.toString()+"\n "+z.precio.toString()+" €")
        builder.setPositiveButton("Comprar",this)
        builder.setNegativeButton("Cancelar",this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                //Aceptar
                /*StyleableToast.makeText(
                    requireContext(),
                    "Introduce tus credenciales",
                    R.style.ColoredBackground)*/
                //Vamos a mandar el zapato que hemos seleccionado de uelta a la actividad menu, para desde ahi poder pasarlo al
                //fragment de carrito
                listener.onDialogPositiveClick(z)

            }
            -2->{
                //Cancelar
            }
        }
    }
    interface MyDialogListener {
        fun onDialogPositiveClick(z: Calzado)
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