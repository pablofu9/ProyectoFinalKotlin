package com.example.practicafinal.dialogs.dialogsUser

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.interfaces.OnDeleteConfirmListener
import com.example.practicafinal.model.Calzado
import com.google.android.material.dialog.MaterialAlertDialogBuilder

//Alert dialog con material design
class DialogComprar(zapato:Calzado): DialogFragment(),DialogInterface.OnClickListener {
    private var z:Calzado = zapato
    private lateinit var listener: MyDialogListener
    private var mListener: OnDialogDismissListener? = null
    private var onDeleteConfirmListener: OnDeleteConfirmListener? = null
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

                //Vamos a mandar el zapato que hemos seleccionado de uelta a la actividad menu, para desde ahi poder pasarlo al
                //fragment de carrito
                listener.onDialogPositiveClick(z)
                //onDeleteConfirmListener?.onDeleteConfirmed()
                dismiss()
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mListener?.onDismiss()
    }
    interface OnDialogDismissListener {
        fun onDismiss()
    }
}