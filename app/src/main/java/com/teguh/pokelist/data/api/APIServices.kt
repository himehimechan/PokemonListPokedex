package com.teguh.pokelist.data.api

import com.teguh.pokelist.data.model.PokemonDetail
import com.teguh.pokelist.data.model.PokemonList
import retrofit2.Call
import retrofit2.http.*

interface APIServices {

    interface Pokemon {
        @GET("pokemon")
        fun pokemonList(@Query("offset") offset: String,
                        @Query("limit") limit: String): Call<PokemonList>

        @GET("pokemon/{idPokemon}")
        fun pokemonDetail(@Path("idPokemon") idPokemon: String): Call<PokemonDetail>
    }

}