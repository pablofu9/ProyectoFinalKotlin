package com.example.practicafinal.dialogs

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.fragments.FragmentCarrito
import com.example.practicafinal.fragments.FragmentProductos
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentUsers
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentZapatillas
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.User
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogVaciar(z:Calzado): DialogFragment(),DialogInterface.OnClickListener {
    private var zapato:Calzado = z
    private lateinit var listener: DialogVaciar.MyDialogListener
    private var onDismissListener: DialogInterface.OnDismissListener? = null
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
    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //Refrescamos el fragment, asi volvemos a cargar la info
        val fragment = parentFragmentManager.findFragmentById(R.id.frame1) as FragmentCarrito
        //Cuando añadidimos refrescamos la peticion get y leemos el arraylist de nuevo
        fragment.vaciar()
        val manager = parentFragmentManager.beginTransaction()
        manager.replace(R.id.frame1,fragment)
        manager.commit()
        onDismissListener?.onDismiss(dialog)
    }



}