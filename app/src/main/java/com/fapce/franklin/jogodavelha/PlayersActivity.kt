package com.fapce.franklin.jogodavelha


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_players.*

class PlayersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_players)

        btnJogar.setOnClickListener { // Verificar se os campos estão vazios ###########################################

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("jogador1", edtJogador1.text.toString())
            intent.putExtra("jogador2", edtJogador2.text.toString())
            startActivity(intent)
        }

        btnConfiguracoes.setOnClickListener {
            val intent = Intent(this, FileActivity::class.java)
            startActivity(intent)
        }

    }
}
