package com.fapce.franklin.jogodavelha

import android.content.ContentValues
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit")
        builder.setMessage("Você tem certeza que quer sair do jogo?")
        builder.setPositiveButton("SIM", { _ , _: Int ->
            finish()
        })
        builder.setNegativeButton("NÃO", { _ , _: Int ->
            Toast.makeText(this, "Vamos jogar!", Toast.LENGTH_SHORT).show()
        })
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        txtJogador1.text = intent.getStringExtra("jogador1")
        txtJogador2.text = intent.getStringExtra("jogador2")
    }



    fun buttonClick(view: View) {
        val btSelected = view as Button
        var cellId = 0

        when (btSelected.id) {
            R.id.bt01 -> cellId = 1
            R.id.bt02 -> cellId = 2
            R.id.bt03 -> cellId = 3
            R.id.bt04 -> cellId = 4
            R.id.bt05 -> cellId = 5
            R.id.bt06 -> cellId = 6
            R.id.bt07 -> cellId = 7
            R.id.bt08 -> cellId = 8
            R.id.bt09 -> cellId = 9
        }

//        Toast.makeText(this, "Id: " + cellId, Toast.LENGTH_SHORT).show()

        playJV(cellId, btSelected)
    }

    // Array de Jogadas
    var playerOne = ArrayList<Int>()
    var playerTwo = ArrayList<Int>()

    // Turno de Jogador Padrão
    var activeplayer = 1

    var verify = 0
    var hasWinner = 0

    fun playJV(cellId:Int, btSelected:Button) {
        if (activeplayer == 1) {
            btSelected.text = "X"
            btSelected.setTextColor(Color.RED)
//            btSelected.setBackgroundColor(Color.YELLOW)
            playerOne.add(cellId)
            activeplayer = 2
        } else {
            btSelected.text = "O"
            btSelected.setTextColor(Color.BLUE)
//            btSelected.setBackgroundColor(Color.RED)
            playerTwo.add(cellId)
            activeplayer = 1
        }
        btSelected.isEnabled = false

        if (verify >= 4) {
            checkWinner()
            if (hasWinner == 1) {
                choice()
            }
        }
        verify++

        var toast:Toast
        if (verify == 9 && hasWinner == 0) { // Empate
            saveScore(intent.getStringExtra("jogador1"), intent.getStringExtra("jogador2"), "Empate")

            toast = Toast.makeText(this, "Empate!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 50)
            toast.show()
            choice()
        }
    }

    fun checkWinner() {
        var winner = 0

        if (playerOne.contains(1) && playerOne.contains(2) && playerOne.contains(3) || // Linha 1
            playerOne.contains(4) && playerOne.contains(5) && playerOne.contains(6) || // Linha 2
            playerOne.contains(7) && playerOne.contains(8) && playerOne.contains(9) || // Linha 3

            playerOne.contains(1) && playerOne.contains(4) && playerOne.contains(7) || // Coluna 1
            playerOne.contains(2) && playerOne.contains(5) && playerOne.contains(8) || // Coluna 2
            playerOne.contains(3) && playerOne.contains(6) && playerOne.contains(9) || // Coluna 3

            playerOne.contains(1) && playerOne.contains(5) && playerOne.contains(9) || // Diagonal Principal
            playerOne.contains(3) && playerOne.contains(5) && playerOne.contains(7) // Diagonal Secundária
        ) {
            winner = 1
        } else {
            if (playerTwo.contains(1) && playerTwo.contains(2) && playerTwo.contains(3) || // Linha 1
                playerTwo.contains(4) && playerTwo.contains(5) && playerTwo.contains(6) || // Linha 2
                playerTwo.contains(7) && playerTwo.contains(8) && playerTwo.contains(9) || // Linha 3

                playerTwo.contains(1) && playerTwo.contains(4) && playerTwo.contains(7) || // Coluna 1
                playerTwo.contains(2) && playerTwo.contains(5) && playerTwo.contains(8) || // Coluna 2
                playerTwo.contains(3) && playerTwo.contains(6) && playerTwo.contains(9) || // Coluna 3

                playerTwo.contains(1) && playerTwo.contains(5) && playerTwo.contains(9) || // Diagonal Principal
                playerTwo.contains(3) && playerTwo.contains(5) && playerTwo.contains(7) // Diagonal Secundária
            ) {
                winner = 2
            }
        }

        var toast:Toast

        if (winner != 0) {
            if (winner == 1) {
                toast = Toast.makeText(this, "O jogador 1 venceu!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 50)
                toast.show()
                saveScore(intent.getStringExtra("jogador1"), intent.getStringExtra("jogador2"), intent.getStringExtra("jogador1"))
            } else if (winner == 2) {
                toast = Toast.makeText(this, "O jogador 2 venceu!", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 50)
                toast.show()
                saveScore(intent.getStringExtra("jogador1"), intent.getStringExtra("jogador2"), intent.getStringExtra("jogador2"))
            }
            hasWinner = 1
        }
    }

    private val sqlHelper = SqlHelper(this)
    fun saveScore(jogador1:String, jogador2:String, vencedor:String) {
        val bd = sqlHelper.writableDatabase

        val contentValues = ContentValues().apply {
            put(TBL_JOGOS_JOGADOR_1, jogador1)
            put(TBL_JOGOS_JOGADOR_2, jogador2)
            put(TBL_JOGOS_VENCEDOR, vencedor)
        }

        val id = bd.insert(
            TBL_JOGOS, null, contentValues
        )

        if (id != -1L) {
            Toast.makeText(
                this, "Placar inserido com sucesso! \n " +
                        "O jogador $vencedor foi o vencedor \n ID: $id", Toast.LENGTH_LONG
            ).show()
        }

        bd.close()
    }

    fun restartGame() {
        playerOne.clear()
        playerTwo.clear()
        hasWinner = 0
        verify = 0
        activeplayer = 1
        resetButton(bt01)
        resetButton(bt02)
        resetButton(bt03)
        resetButton(bt04)
        resetButton(bt05)
        resetButton(bt06)
        resetButton(bt07)
        resetButton(bt08)
        resetButton(bt09)
    }

    fun resetButton (bt:Button){
        bt.isEnabled = true
//        bt.setBackgroundColor(Color.LTGRAY)
        bt.text = ""
    }

    fun choice() {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle("Jogar Novamente")
        builder.setMessage("Você gostou, quer jogar novamente?")
        builder.setPositiveButton("SIM", { _ , _: Int ->
            restartGame()
        })
        builder.setNegativeButton("NÂO", { _ , _: Int ->
            finish()
        })
        builder.show()
    }
}



