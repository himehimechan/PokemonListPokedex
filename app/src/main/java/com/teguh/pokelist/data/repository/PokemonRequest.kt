package com.teguh.pokelist.data.repository

import com.teguh.pokelist.data.api.APICaller
import com.teguh.pokelist.data.api.APIServices
import com.teguh.pokelist.data.model.PokemonDetail
import com.teguh.pokelist.data.model.PokemonList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRequest {

    fun pokemonList(offset: String, callback: (PokemonList) -> Unit) {
        val request = APICaller.shared.buildService(APIServices.Pokemon::class.java)
        val caller = request.pokemonList(offset, "20")
        caller.enqueue(object: Callback<PokemonList> {
            override fun onFailure(call: Call<PokemonList>, t: Throwable) {
                callback(PokemonList())
            }

            override fun onResponse(call: Call<PokemonList>, response: Response<PokemonList>) {
                response.body()?.let {
                    return callback(it)
                }
                response.errorBody()?.string()?.let {
                    return callback(PokemonList())
                }
                callback(PokemonList())
            }

        })
    }

    fun pokemonDetail(name: String, callback: (PokemonDetail) -> Unit) {
        val request = APICaller.shared.buildService(APIServices.Pokemon::class.java)
        val caller = request.pokemonDetail(name)
        caller.enqueue(object: Callback<PokemonDetail> {
            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                callback(PokemonDetail())
            }

            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                response.body()?.let {
                    return callback(it)
                }
                response.errorBody()?.string()?.let {
                    return callback(PokemonDetail())
                }
                callback(PokemonDetail())
            }

        })
    }

}