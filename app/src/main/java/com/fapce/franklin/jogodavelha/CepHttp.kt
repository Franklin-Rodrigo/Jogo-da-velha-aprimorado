package com.fapce.franklin.jogodavelha

import android.content.Context
import android.net.ConnectivityManager
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


object CepHttp {
//    val PAY_JSON = "https://viacep.com.br/ws/%s/json/"
    val PAY_JSON = "https://tadeupdm.herokuapp.com/%s"

    @Throws(IOException::class)
    private fun connect(urlAddress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAddress)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }
        connection.connect()
        return connection
    }

    fun hasConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = connectivityManager.activeNetwork // build.gradle (Module: app) - minSdkVersion estava com valor 21, mas requer 23
        return networkCallback != null
    }


    fun loadCep(pay: String?): Pay? {
        try {
            var url_cep = PAY_JSON.format(pay)
            val connection = connect(url_cep)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val json = JSONObject(streamToString(inputStream))
                return readCepFromJson(json)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(JSONException::class)
    fun readCepFromJson(json: JSONObject): Pay {
        val pay = Pay(
            json.getString("numero_cartao"),
            json.getString("nome_cliente"),
            json.getString("bandeira"),
            json.getString("cod_seguranca"),
            json.getString("valor_em_centavos")
        )
        return pay
    }

    @Throws(IOException::class)
    private fun streamToString(inputStream: InputStream): String {
        val buffer = ByteArray(1024)
        // O bigBuffer vai armazenar todos os bytes lidos
        val bigBuffer = ByteArrayOutputStream()
        // Precisamos saber quantos bytes foram lidos
        var bytesRead: Int
        // Vamos lendo de 1KB por vez...
        while (true) {
            bytesRead = inputStream.read(buffer)
            if (bytesRead == -1) break
            // Copiando a quantidade de bytes lidos do buffer para o bigBuffer
            bigBuffer.write(buffer, 0, bytesRead)
        }
        return String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }

}
