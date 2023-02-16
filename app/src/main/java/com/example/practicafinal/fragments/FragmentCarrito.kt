package com.example.practicafinal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.NumberPicker
import android.widget.TextView
import com.example.practicafinal.R
import com.example.practicafinal.dialogs.DialogComprar
import com.example.practicafinal.dialogs.DialogVaciar
import com.example.practicafinal.model.Calzado
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView


class FragmentCarrito : Fragment(), View.OnClickListener {

    private lateinit var txtMarca:TextView
    private lateinit var txtCarritoMarca:TextView
    private lateinit var txtCarritoModelo:TextView
    private lateinit var txtCarritoTalla:TextView
    private lateinit var txtCarritoPrecio:TextView
    private lateinit var txtCarritoCantidad:TextView
    private lateinit var txtCarritoTotal:TextView
    private lateinit var txtModelo:TextView
    private lateinit var txtTalla:TextView
    private lateinit var txtPrecio:TextView
    private lateinit var txtCantidad:NumberPicker
    private lateinit var txtTotal:TextView
    private lateinit var vacio:ShimmerTextView

    private lateinit var btnPagar:ImageButton
    private lateinit var btnVaciar:ImageButton



    var shimmer = Shimmer()
    var carritoVacio:Boolean=false
    private var zapato: Calzado? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_carrito,container,false)
        txtMarca = view.findViewById(R.id.txtMarca)
        txtModelo = view.findViewById(R.id.txtModelo)
        txtTalla = view.findViewById(R.id.txtTalla)
        txtPrecio = view.findViewById(R.id.txtPrecio)
        txtCantidad = view.findViewById(R.id.txtCantidad)

        txtCarritoMarca = view.findViewById(R.id.txtCarritoMarca)
        txtCarritoModelo = view.findViewById(R.id.txtCarritoModelo)
        txtCarritoTalla = view.findViewById(R.id.txtCarritoTalla)
        txtCarritoCantidad = view.findViewById(R.id.txtCarritoCantidad)
        txtCarritoPrecio = view.findViewById(R.id.txtCarritoPrecio)
        txtCarritoTotal = view.findViewById(R.id.txtCarritoTotal)
        txtTotal = view.findViewById(R.id.txtTotal)

        vacio=view.findViewById(R.id.vacio)

        btnPagar=view.findViewById(R.id.btnPagar)
        btnVaciar=view.findViewById(R.id.btnVaciar)
        btnPagar.setOnClickListener(this)
        btnVaciar.setOnClickListener(this)
        setCantidad()
        carritoVacio()



        return view
    }


    fun setCantidad(){
        txtCantidad.minValue=1
        txtCantidad.maxValue=5
        txtCantidad.wrapSelectorWheel=true
        txtCantidad.value=1
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnVaciar->{
                showDialog(zapato!!)
            }
            R.id.btnPagar->{

            }
        }
    }


    fun carritoVacio(){
        if(arguments!=null){
            shimmer.cancel()
            var suma:Int=0
            zapato = arguments?.getSerializable("zapato") as Calzado

            txtMarca.text=zapato?.marca
            txtModelo.text=zapato?.modelo
            txtTalla.text=zapato?.talla.toString()
            txtPrecio.text=zapato?.precio.toString() + " €"
            txtCantidad.setOnValueChangedListener { picker, oldVal, newVal ->
                suma=newVal* zapato!!.precio
                txtTotal.text="$suma" + " €"
            }
        }else{
            //Si no recibimos argumento, es que el carrito esta vacio. Se mostrara un mensaje de carrito vacio.
            zapato=null
            isVacio()
        }
    }
    fun isVacio(){
        vacio.visibility=View.VISIBLE
        shimmer.start(vacio)
        txtCarritoMarca.visibility=View.GONE
        txtCarritoModelo.visibility=View.GONE
        txtCarritoTalla.visibility=View.GONE
        txtCarritoCantidad.visibility=View.GONE
        txtCarritoPrecio.visibility=View.GONE
        txtMarca.visibility=View.GONE
        txtModelo.visibility=View.GONE
        txtTalla.visibility=View.GONE
        txtPrecio.visibility=View.GONE
        txtCantidad.visibility=View.GONE
        txtCarritoTotal.visibility=View.GONE
        txtTotal.visibility=View.GONE
        btnPagar.visibility=View.GONE
        btnVaciar.visibility=View.GONE
    }
    fun showDialog(c:Calzado){
        val vaciar = DialogVaciar(c)
        activity?.let { vaciar.show(it.supportFragmentManager, "vaciar") }
    }


}