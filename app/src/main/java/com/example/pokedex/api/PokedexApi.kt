package com.example.pokedex.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class PokemonListResponse(val results: List<PokemonResponse>)

data class PokemonResponse(
    val name: String,
    val url: String
)

interface PokedexApi {
    @GET("pokemon?limit=100") // Ajuste conforme necessário
    fun getPokemonList(): Call<PokemonListResponse>

    @GET("pokemon/{name}")
    fun getPokemonDetails(@Path("name") name: String): Call<PokemonDetails>
}
