package com.teguh.pokelist.ui.view.detail_pokemon

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teguh.pokelist.data.db_helper.DataBaseHandler
import com.teguh.pokelist.data.model.PokemonDetail
import com.teguh.pokelist.data.model.Results
import com.teguh.pokelist.data.repository.PokemonRequest
import com.teguh.pokelist.utils.MyContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

class DetailPokemonViewModel: ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)
    val alertInfo: MutableLiveData<String> = MutableLiveData()
    val resultDataDetailPokemon: MutableLiveData<PokemonDetail> = MutableLiveData()
    val db = DataBaseHandler(MyContext.context!!)
    var isFound = false
    var nameFromIntent = ""

    fun loadPokemonList(name: String){
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            PokemonRequest().pokemonDetail(name) {
                isLoading.postValue(false)
                isFound = db.readSingleDataMyPokemon(it.id.toString())
                resultDataDetailPokemon.postValue(it)
            }
        }
    }
}