package com.example.practicafinal.dialogs.dialogsAdmin

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

class DialogUpdateUser(private var u: User):DialogFragment(),OnClickListener {
    private lateinit var edUser: TextInputEditText
    private lateinit var edPass: TextInputEditText
    private lateinit var edEmail: TextInputEditText
    private var onDismissListener: DialogInterface.OnDismissListener? = null
    var user: User = User()
    val serviceUser = UserService()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = context?.let { MaterialAlertDialogBuilder(it) }
        val inflater=requireActivity().layoutInflater
        val view: View =inflater.inflate(R.layout.addusers,null)
        builder?.setView(view)
        edUser=view.findViewById(R.id.edUser)
        edPass=view.findViewById(R.id.edPass)
        edEmail=view.findViewById(R.id.edEmail)
        builder?.setTitle("Modificar user")
        builder?.setPositiveButton("Modificar",this)
        builder?.setNegativeButton("Volver",this)

        edUser.setText(u.name)
        edPass.setText(u.pass)
        edEmail.setText(u.email)
        return builder?.create()!!

    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        when(which){
            -1->{
                user=User(u.id,edUser.text.toString(),edPass.text.toString(),edEmail.text.toString(),u.admin)
                updateUser(user)
                context?.let {
                    StyleableToast.makeText(
                        it,
                        "Usuario modificado con exito",
                        R.style.ColoredBackgroundGreen
                    ).show()
                    /**
                     * En el boton aceptas, modificamos, cogemos el usuario que nos viene desde el login
                     * y al ser administradores, podemos cambiarle los campos que queramos
                     */
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
    fun updateUser(u: User) {
        serviceUser.updateUser(u)
    }
}