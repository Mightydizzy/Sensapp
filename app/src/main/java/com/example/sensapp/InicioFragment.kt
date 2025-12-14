package com.example.sensapp
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import android.util.Base64

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var txtMovimientos: TextView
    private lateinit var txtUltimaAlerta: TextView
    private lateinit var txtSensores: TextView
    private lateinit var txtEstado: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtMovimientos   = view.findViewById(R.id.kpiMovimientos)
        txtUltimaAlerta  = view.findViewById(R.id.kpiUltimaAlerta)
        txtSensores      = view.findViewById(R.id.kpiSensores)
        txtEstado        = view.findViewById(R.id.kpiEstado)

        cargarEstado()
    }

    private fun cargarEstado() {
        // ⚠ Cambia esta IP por la IP real que imprime el ESP32 en el monitor serie
        val baseUrl = "http://192.168.0.123"
        val url = "$baseUrl/status"

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val jsonStr = httpGetWithBasicAuth(url, "admin", "12345")
                val obj = JSONObject(jsonStr)

                val movimientos = obj.optInt("movimientos", 0)
                val ultima      = obj.optString("ultimaAlerta", "—")
                val sensores    = obj.optInt("sensoresActivos", 0)
                val estado      = obj.optString("estado", "—")

                withContext(Dispatchers.Main) {
                    txtMovimientos.text  = "Movimientos: $movimientos"
                    txtUltimaAlerta.text = "Última alerta: $ultima"
                    txtSensores.text     = "Sensores activos: $sensores"
                    txtEstado.text       = "Estado: $estado"
                }

            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    txtEstado.text = "Error: ${e.message}"
                }
            }
        }
    }

    private fun httpGetWithBasicAuth(urlStr: String, user: String, pass: String): String {
        val url = URL(urlStr)
        val conn = url.openConnection() as HttpURLConnection
        conn.connectTimeout = 5000
        conn.readTimeout = 5000
        conn.requestMethod = "GET"

        // Basic Auth: admin:12345
        val creds = "$user:$pass"
        val basic = "Basic " + Base64.encodeToString(creds.toByteArray(), Base64.NO_WRAP)
        conn.setRequestProperty("Authorization", basic)

        // Estos dos no rompen compatibilidad
        conn.setRequestProperty("Connection", "close")
        conn.setRequestProperty("Accept-Encoding", "identity")

        val code = conn.responseCode
        val stream = if (code in 200..299) conn.inputStream else conn.errorStream
        val body = stream.bufferedReader().use { it.readText() }
        conn.disconnect()

        if (code !in 200..299) {
            throw IOException("HTTP $code: $body")
        }

        return body
    }
}
