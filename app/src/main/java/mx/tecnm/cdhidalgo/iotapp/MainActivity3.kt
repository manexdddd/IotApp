package mx.tecnm.cdhidalgo.iotapp

import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class MainActivity3 : AppCompatActivity() {
    lateinit var tvNewId:TextView
    lateinit var etNetName:EditText
    lateinit var etNetType:EditText
    lateinit var  etNetValue: EditText
    lateinit var  btnNewCancel:Button
    lateinit var  btnNewSave:Button

    lateinit var  sesion : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
            tvNewId = findViewById(R.id.etNewId)
        etNetName = findViewById(R.id.etNewName)
        etNetType= findViewById(R.id.etNewType)
        etNetValue = findViewById(R.id.etNewValue)
        btnNewCancel = findViewById(R.id.btnNewCancel)
        btnNewSave = findViewById(R.id.btnNewSave)

        sesion = getSharedPreferences("sesion",0)

        btnNewCancel.setOnClickListener{
            finish()
        }


        if(intent.extras != null){
            tvNewId.text = intent.extras!!.getString("id")
            etNetName.setText (intent.extras!!.getString("name"))
            etNetType.setText (intent.extras!!.getString("type"))
            etNetValue.setText (intent.extras!!.getString("value"))
           btnNewSave.setOnClickListener {
               saveChanges()
           }




        }else{

            btnNewSave.setOnClickListener {
                saveNew()
            }
        }
    }

    private fun saveNew() {
        val url = Uri.parse(Config.URL + "sensors")//revisa que la url esta bien escrita.
            .buildUpon()
            .build().toString()


        val body  = JSONObject()
        body.put("name",etNetName.text.toString())
        body.put("type",etNetType.text.toString())
        body.put("value",etNetValue.text.toString())
        val peticion = object: JsonObjectRequest(Request.Method.POST, url, body, {
                response -> Toast.makeText(this,"Guardado",Toast.LENGTH_LONG).show()
            finish()


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

    private fun saveChanges() {
        val url = Uri.parse(Config.URL + "sensors/"+tvNewId.text)//revisa que la url esta bien escrita.
            .buildUpon()
            .build().toString()


        val body  = JSONObject()
        body.put("name",etNetName.text.toString())
        body.put("type",etNetType.text.toString())
        body.put("value",etNetValue.text.toString())
        val peticion = object: JsonObjectRequest(Request.Method.PUT, url, body, {
                response -> Toast.makeText(this,"Guardado",Toast.LENGTH_LONG).show()
            finish()


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
}