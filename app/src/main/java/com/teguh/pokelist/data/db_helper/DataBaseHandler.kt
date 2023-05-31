package com.teguh.pokelist.data.db_helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.teguh.pokelist.data.model.PokemonListLocal

const val DATABASENAME = "pokedex"
const val TABLENAME = "my_pokemon"

const val idColumn = "id"
const val nameColumn = "name"
const val nickNameColumn = "nick_name"

class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLENAME + " (" +
                idColumn + " VARCHAR(100)," + nameColumn + " VARCHAR(100)," +
                nickNameColumn + " VARCHAR(100))"
        db?.execSQL(createTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        onCreate(db);
    }
    fun insertDataMyPokemon(id: String, name: String, nickName: String) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(idColumn, id)
        contentValues.put(nameColumn, name)
        contentValues.put(nickNameColumn, nickName)
        val result = database.insert(TABLENAME, null, contentValues)
        if (result == (0).toLong()) {
            Log.e("sqlite", "insertDataSessionFailed")
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        else {
            Log.e("sqlite", "insertDataSessionSuccess")
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("Range")
    fun readDataMyPokemon(): MutableList<PokemonListLocal> {
        val list: MutableList<PokemonListLocal> = ArrayList()
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val data = PokemonListLocal(
                    id = result.getString(result.getColumnIndex(idColumn)),
                    name = result.getString(result.getColumnIndex(nameColumn)),
                    nickName = result.getString(result.getColumnIndex(nickNameColumn))
                )
                list.add(data)
            }
            while (result.moveToNext())
        }
        return list
    }
    fun deleteMyPokemon(data: String) {
        val database = this.readableDatabase
        database.delete(TABLENAME, "$idColumn=$data", null)
    }
    fun readSingleDataMyPokemon(id: String): Boolean {
        var isFound: Boolean = false
        val db = this.readableDatabase
        val query = "Select * from $TABLENAME where $idColumn=$id"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                isFound = true
            }
            while (result.moveToNext())
        }
        return isFound
    }
}