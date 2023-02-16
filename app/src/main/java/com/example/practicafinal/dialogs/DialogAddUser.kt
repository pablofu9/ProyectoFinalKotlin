package com.example.practicafinal.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.practicafinal.R
import com.example.practicafinal.fragments.fragmentsAdmin.FragmentUsers
import com.example.practicafinal.model.User
import com.example.practicafinal.services.UserService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import io.github.muddz.styleabletoast.StyleableToast
import java.nio.file.attribute.AclEntry.Builder

class DialogAddUser:DialogFragment(),OnClickListener {
    private lateinit var edUser:TextInputEditText
    private lateinit var edPass:TextInputEditText
    private lateinit var edEmail:TextInputEditText
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    val serviceUser = UserService()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val inflater=requireActivity().layoutInflater
        val view: View =inflater.inflate(R.layout.addusers,null)
        builder?.setView(view)

        edUser=view.findViewById(R.id.edUser)
        edPass=view.findViewById(R.id.edPass)
        edEmail=view.findViewById(R.id.edEmail)

        builder?.setTitle("Añadir users")
        builder?.setPositiveButton("Añadir",this)
        builder?.setNegativeButton("Volver",this)
        return builder?.create()!!
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                val u:User=User(edUser.text.toString(),edPass.text.toString(),edEmail.text.toString(),false)
                createUser(u)
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Usuario añadido",
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
        val fragment = parentFragmentManager.findFragmentById(R.id.frame1) as FragmentUsers
        //Cuando añadidimos refrescamos la peticion get y leemos el arraylist de nuevo
        fragment.leer()
        fragment.reload()
        Log.d("huevo","h")
        onDismissListener?.onDismiss(dialog)
    }
    fun createUser(u:User){
        serviceUser.createUser(u)
    }
}