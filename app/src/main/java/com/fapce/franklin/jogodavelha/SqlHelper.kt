package com.fapce.franklin.jogodavelha

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqlHelper(context: Context) : SQLiteOpenHelper(context,
    BD_NOME, null,
    BD_VERSAO
) {

    //// /data/data/com.example.sharedpreferences/databases/PDM_SQLite
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(
            "CREATE TABLE $TBL_USUARIO (" + //
                    "$TBL_USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + //
                    "$TBL_USUARIO_LOGIN TEXT NOT NULL, " + //
                    "$TBL_USUARIO_SENHA TEXT NOT NULL )"//
        )
        sqLiteDatabase.execSQL(
            "CREATE TABLE $TBL_JOGOS (" + //
                    "$TBL_JOGOS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + //
                    "$TBL_JOGOS_JOGADOR_1 TEXT NOT NULL, " + //
                    "$TBL_JOGOS_JOGADOR_2 TEXT NOT NULL, " + //
                    "$TBL_JOGOS_VENCEDOR TEXT NOT NULL)" //
        )
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
