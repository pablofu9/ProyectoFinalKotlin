package com.example.practicafinal.dialogs.dialogsUser

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.fragments.fragmentsUser.FragmentCarrito
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.model.Factura
import com.example.practicafinal.model.User
import com.example.practicafinal.services.FacturaService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.muddz.styleabletoast.StyleableToast

class DialogPagar(private var user:User,private val zapato:Calzado,private var cantidad:Int,private var suma:Int):DialogFragment(),OnClickListener {
    val serviceFactura=FacturaService()

    private var onDismissListener: DialogInterface.OnDismissListener? = null
    private lateinit var listener: DialogPagar.MyDialogListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setTitle("Seguro que quieres pagar")
        builder.setMessage("Total a pagar = "+suma+" €")
        builder.setPositiveButton("Aceptar",this)
        builder.setNegativeButton("Cancelar",this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                //Generamos una factura y luego la introducimos
                var f1:Factura= Factura(user.id,zapato.id_calzado,cantidad,suma)
                createFactura(f1)
                listener.onDialogPositiveClick3()
                StyleableToast.makeText(
                    requireContext(),
                    "Gracias por su compra",
                    R.style.ColoredBackgroundGreen
                ).show()
                val fragment = parentFragmentManager.findFragmentById(R.id.frame1) as FragmentCarrito
                //Cuando añadidimos refrescamos la peticion get y leemos el arraylist de nuevo

                val manager = parentFragmentManager.beginTransaction()
                fragment.vaciar()
                fragment.carritoVacio()
                fragment.arguments=null
                manager.replace(R.id.frame1,fragment)
                manager.commit()
            }
            -2->{

            }
        }
    }
    fun createFactura(f: Factura){
        serviceFactura.createFactura(f)
    }
    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //Refrescamos el fragment, asi volvemos a cargar la info

        onDismissListener?.onDismiss(dialog)
    }

    interface MyDialogListener {
        fun onDialogPositiveClick3()
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