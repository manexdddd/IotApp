package mx.tecnm.cdhidalgo.iotapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    lateinit var etLoginUser: EditText
    lateinit var etLoginPass: EditText
    lateinit var btnLoginStart: Button
    lateinit var sesion: SharedPreferences //guarda o lee datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //metodo heredado
        setContentView(R.layout.activity_main) //esta clase, usa esta vista

        etLoginUser = findViewById(R.id.etLoginUser)
        etLoginPass = findViewById(R.id.etLoginPass)
        btnLoginStart = findViewById(R.id.btnLoginStart)
        sesion = getSharedPreferences("sesion", 0)
        btnLoginStart.setOnClickListener { login() }
    }

    private fun login() {
        val url = Uri.parse(Config.URL + "login")//revisa que la url esta bien escrita.
            .buildUpon()
            .build().toString()

        val peticion = object: StringRequest(Request.Method.POST, url, {
                response -> with(sesion.edit()) {
            putString("jwt", response) //clave, valor   y guarda el token
            putString("username", etLoginUser.text.toString())
            apply()
        }
            startActivity(Intent(this, MainActivity2::class.java))
        }, {
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show()
        }) {
            override fun getParams(): Map<String, String>{
                val body: MutableMap<String, String> = HashMap()
                body["username"] = etLoginUser.text.toString()
                body.put("password", etLoginPass.text.toString())
                return body
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)

    }
}