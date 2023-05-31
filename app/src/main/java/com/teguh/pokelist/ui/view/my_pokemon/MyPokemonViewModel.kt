package com.teguh.pokelist.ui.view.my_pokemon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teguh.pokelist.data.db_helper.DataBaseHandler
import com.teguh.pokelist.data.model.PokemonListLocal
import com.teguh.pokelist.utils.MyContext

class MyPokemonViewModel: ViewModel() {
    var myPokemonLocalList: MutableLiveData<List<PokemonListLocal>> = MutableLiveData()
    val db = DataBaseHandler(MyContext.context!!)

    init {
        loadMyPokemon()
    }

    fun loadMyPokemon(){
        val data = db.readDataMyPokemon()
        myPokemonLocalList.postValue(data)
    }

}