package com.teguh.pokelist.ui.view.list_pokemon

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teguh.pokelist.data.model.Results
import com.teguh.pokelist.data.repository.PokemonRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

class ListPokemonViewModel: ViewModel() {
    var isLoading = MutableLiveData<Boolean>(false)
    val alertInfo: MutableLiveData<String> = MutableLiveData()
    val resultDataPokemon: MutableLiveData<List<Results>> = MutableLiveData()
    val listDataForView = mutableListOf<Results>()
    var nextOffset: String = ""

    init {
        loadPokemonList("0")
    }

    fun loadPokemonList(param: String){
        isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            PokemonRequest().pokemonList( param) {
                isLoading.postValue(false)
                Log.d("data", it.count.toString())

                if (it.results.isNotEmpty()) {
                    if(it.next != null){
                        val uri = URI(it.next)
                        val query = uri.query
                        val params = query.split("&")
                        val paramMap = mutableMapOf<String, String>()

                        for (param in params) {
                            val keyValue = param.split("=")
                            if (keyValue.size == 2) {
                                val key = keyValue[0]
                                val value = keyValue[1]
                                paramMap[key] = value
                            }
                        }

                        val offset = paramMap["offset"]
                        val limit = paramMap["limit"]
                        nextOffset = offset?:""
                    }
                    resultDataPokemon.postValue(it.results)
                } else {
                    alertInfo.postValue("Gagal Load Data")
                }
            }
        }
    }

}