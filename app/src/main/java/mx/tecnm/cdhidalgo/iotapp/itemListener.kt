package mx.tecnm.cdhidalgo.iotapp

import android.view.View

interface itemListener {
    fun onClick(v: View?, position:Int)
    fun onEdit(v: View?, position:Int)
    fun onDel(v: View?, position:Int)


}