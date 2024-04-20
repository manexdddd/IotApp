package mx.tecnm.cdhidalgo.iotapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest

class MainActivity2 : AppCompatActivity(),itemListener {
    private lateinit var rvList: RecyclerView
    private lateinit var btnAdd:Button
    private lateinit var btnRefresh:Button
    lateinit var sesion: SharedPreferences //guarda o lee datos
    lateinit var  lista:Array<Array<String?>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        rvList = findViewById(R.id.rvList)
        btnAdd = findViewById(R.id.btnAdd)
        btnRefresh = findViewById(R.id.btnRefresh)
        sesion = getSharedPreferences("sesion", 0)
        //3 minimos para que funcione el recycler
        rvList.setHasFixedSize(true) //bloquea el tamaño de ventana
        rvList.itemAnimator = DefaultItemAnimator() //animacion al mover los items
        rvList.layoutManager = LinearLayoutManager(this)

        fill()
           btnAdd.setOnClickListener{
               startActivity(Intent(this,MainActivity3::class.java))

           }

        btnRefresh.setOnClickListener{
          fill()

        }
    }
    private fun fill(){
        val url = Uri.parse(Config.URL + "sensors")//revisa que la url esta bien escrita.
            .buildUpon()
            .build().toString()

        val peticion = object: JsonObjectRequest(Request.Method.GET, url, null, {
                response -> val data = response.getJSONArray("data")
            lista = Array(data.length()){ arrayOfNulls<String>(5)

            }
            for(i in 0 until data.length()){
                lista[i][0] = data.getJSONObject(i).getString("id")
                lista[i][1] = data.getJSONObject(i).getString("name")
                lista[i][2] = data.getJSONObject(i).getString("type")
                lista[i][3] = data.getJSONObject(i).getString("value")
                lista[i][4] = data.getJSONObject(i).getString("date")
            }
              rvList.adapter = MyAdapter(lista,this)

        }, {
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }) {
            override fun getHeaders(): Map<String, String>{
                val body: MutableMap<String, String> = HashMap()
                body["Authorization"] = sesion.getString("jwt", "").toString() //obtiene el token guardado
                return body
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)

    }

    override fun onClick(v: View?, position: Int) {
        TODO("Not yet implemented")
        Toast.makeText(this,"Click en $position",Toast.LENGTH_SHORT).show()
    }

    override fun onEdit(v: View?, position: Int) {
        val i  = Intent(this,MainActivity3::class.java)
        val intent = Intent(this, MainActivity3::class.java)
        intent.putExtra("id", lista[position][0])
        intent.putExtra("name", lista[position][1])
        intent.putExtra("type", lista[position][2])
        intent.putExtra("value", lista[position][3])
        startActivity(intent)
    }

    override fun onDel(v: View?, position: Int) {
        AlertDialog.Builder(this)
            .setTitle(("Eliminar"))
            .setMessage("seguro de eliminar ${lista[position][1]}?")
            .setPositiveButton("SI"){dialog,which->
                val url = Uri.parse(Config.URL +"sensors/"+ lista[position][0])
                    .buildUpon()
                    .build().toString()
                val peticion = object: StringRequest(Request.Method.DELETE, url, {
                        response ->fill()
                }, {
                        error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
                     fill()
                }) {
                    override fun getHeaders(): Map<String, String>{
                        val body: MutableMap<String, String> = HashMap()
                        body["Authorization"] = sesion.getString("jwt", "").toString() //obtiene el token guardado
                        return body
                    }
                }
                MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)


            }
            .setNegativeButton("NO",null)
            .show()

    }
}