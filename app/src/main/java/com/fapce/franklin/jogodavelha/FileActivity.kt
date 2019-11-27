package com.fapce.franklin.jogodavelha

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_file.*
import java.io.*


class FileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)

        btnSQLite.setOnClickListener {
            val intent = Intent(this, SqlActivity::class.java)
            startActivity(intent)
        }

        btnExcluir.setOnClickListener {
            finish()
        }

    }

    // /data/data/com.example.sharedpreferences/files/arquivoInterno.txt
    private fun salvarInterno() {
        try {

            val arquivo = openFileOutput("arquivoInterno.txt", Context.MODE_PRIVATE)
            salvar(arquivo)

        } catch (exception: Exception) {

            Toast.makeText(
                this, "Erro ao salvar o arquivo: $exception", Toast.LENGTH_LONG
            ).show()

        }
    }

    private fun lerInterno() {
        try {

            val arquivo = openFileInput("arquivoInterno.txt")
            ler(arquivo)

        } catch (exception: Exception) {

            Toast.makeText(
                this, "Erro ao ler o arquivo: $exception", Toast.LENGTH_LONG
            ).show()

        }
    }

    // https://developer.android.com/reference/android/content/Context.html#getExternalFilesDir(java.lang.String)    
    // /sdcard/Android/data/com.example.sharedpreferences/files/arquivoExterno.txt
    private fun salvarExterno() {
        val estado = Environment.getExternalStorageState()
        // Pode ler e escrever
        if (estado == Environment.MEDIA_MOUNTED) {
            try {
                val diretorio = getExternalFilesDir(null)

                if (diretorio?.exists() == false) {
                    diretorio.mkdir()
                }

                val arquivoNome = File(diretorio, "arquivoExterno.txt")
                if (!arquivoNome.exists()) {
                    arquivoNome.createNewFile()
                }

                val arquivo = FileOutputStream(arquivoNome)
                salvar(arquivo)

            } catch (exception: Exception) {

                Toast.makeText(
                    this, "Erro ao salvar o arquivo no SD Card: $exception", Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun lerExterno() {
        val estado = Environment.getExternalStorageState()
        // Pode ler e escrever
        if (estado == Environment.MEDIA_MOUNTED
            // Pode somente ler
            || estado == Environment.MEDIA_MOUNTED_READ_ONLY
        ) {
            try {

                val diretorio = getExternalFilesDir(null)
                if (diretorio?.exists() == true) {

                    val arquivoNome = File(diretorio, "arquivoExterno.txt")
                    if (arquivoNome.exists()) {
                        try {

                            arquivoNome.createNewFile()
                            val arquivo = FileInputStream(arquivoNome)
                            ler(arquivo)

                        } catch (exception: Exception) {

                            Toast.makeText(
                                this,
                                "Erro ao salvar o arquivo no SD Card: $exception",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }


                }

            } catch (exception: Exception) {

                Toast.makeText(
                    this, "Erro ao salvar o arquivo no SD Card: $exception", Toast.LENGTH_LONG
                ).show()

            }
        }

    }

    private fun salvar(arquivo: FileOutputStream) {
        val linhas = TextUtils.split(edtEntrada.text.toString(), "\n")
        val escrever = PrintWriter(arquivo)

        for (linha in linhas) {
            escrever.println(linha)
        }

        escrever.flush()
        escrever.close()
        arquivo.close()
    }

    private fun ler(arquivo: FileInputStream) {
        val ler = BufferedReader(InputStreamReader(arquivo))
        val stringBuilder = StringBuffer()

        do {
            val linha = ler.readLine() ?: break
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("\n")
            }
            stringBuilder.append(linha)
        } while (true)

        ler.close()
        arquivo.close()
        txtLer.text = stringBuilder.toString()
    }

}
