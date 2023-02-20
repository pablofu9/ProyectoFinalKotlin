package com.example.practicafinal.dialogs.dialogsAdmin

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentZapatillas
import com.example.practicafinal.model.Calzado
import com.example.practicafinal.services.CalzadoService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import io.github.muddz.styleabletoast.StyleableToast

class DialogUpdateCalzado(private var c: Calzado): DialogFragment(),OnClickListener {
    var calzadoService= CalzadoService()
    private lateinit var edMarca: TextInputEditText
    private lateinit var edModelo: TextInputEditText
    private lateinit var edPrecio: TextInputEditText
    private lateinit var spinnerTalla: Spinner
    private var onDismissListener: DialogInterface.OnDismissListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val inflater=requireActivity().layoutInflater
        val view: View =inflater.inflate(R.layout.addcalzado,null)
        builder?.setView(view)
        var arrayTallas = arrayOf("39","40","41","42","43","44","45") //Array de las tallas
        edMarca=view.findViewById(R.id.edMarca)
        edModelo=view.findViewById(R.id.edModelo)
        edPrecio=view.findViewById(R.id.edPrecio)
        spinnerTalla=view.findViewById(R.id.spinnerTalla)
        builder?.setTitle("Modificar zapatilla")
        builder?.setPositiveButton("Modificar",this)
        builder?.setNegativeButton("Volver",this)
        var adapter = context?.let { ArrayAdapter(it, com.google.android.material.R.layout.support_simple_spinner_dropdown_item,arrayTallas) }
        spinnerTalla.adapter=adapter
        edMarca.setText(c.marca)
        edModelo.setText(c.modelo)
        edPrecio.setText(c.precio.toString())
        var talladefault = c.talla
        spinnerTalla.setSelection(arrayTallas.indexOf(talladefault.toString()))
        return builder?.create()!!
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                    var c : Calzado = Calzado(c.id_calzado,edMarca.text.toString(),edModelo.text.toString(),
                        spinnerTalla.selectedItem.toString().toInt(),edPrecio.text.toString().toInt())
                updateCalzado(c)
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Zapatilla modificada",
                        R.style.ColoredBackgroundGreen
                    ).show()

                }

                }

            -2->{

            }
        }
    }
    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        //Refrescamos el fragment, asi volvemos a cargar la info
        val fragment = parentFragmentManager.findFragmentById(R.id.frame1) as FragmentZapatillas
        //Cuando añadidimos refrescamos la peticion get y leemos el arraylist de nuevo
        fragment.leerCalzado()
        fragment.leerFacturas()
        fragment.reloadCalzado()
        val manager = parentFragmentManager.beginTransaction()
        manager.replace(R.id.frame1,fragment)
        manager.commit()
        onDismissListener?.onDismiss(dialog)
    }
    fun updateCalzado(c: Calzado) {
        calzadoService.updateCalzado(c)
    }
}