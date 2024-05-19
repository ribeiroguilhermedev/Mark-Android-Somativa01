package com.example.pokedex.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokedex.api.RetrofitInstance
import com.example.pokedex.data.PokemonDatabase
import com.example.pokedex.model.Pokemon
import com.example.pokedex.util.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

    private val pokemonDao = PokemonDatabase.getDatabase(application).pokemonDao()

    private val _pokemonList = MutableLiveData<MutableList<Pokemon>>()
    val pokemonList: LiveData<MutableList<Pokemon>> get() = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getPokemonList().execute()
                }
                if (response.isSuccessful) {
                    val pokemonResponses = response.body()?.results ?: emptyList()
                    val pokemonList = pokemonResponses.map { response ->
                        val detailsResponse = withContext(Dispatchers.IO) {
                            RetrofitInstance.api.getPokemonDetails(response.name).execute()
                        }
                        val details = detailsResponse.body()
                        val isFavorite = withContext(Dispatchers.IO) {
                            pokemonDao.getPokemonByName(details?.name ?: "") != null
                        }
                        Pokemon(
                            name = details?.name ?: "",
                            type = details?.types?.joinToString { it.type.name } ?: "",
                            height = details?.height ?: 0,
                            weight = details?.weight ?: 0,
                            imageUrl = details?.sprites?.front_default ?: "",
                            isFavorite = isFavorite
                        )
                    }.toMutableList()
                    _pokemonList.postValue(pokemonList)
                }
            } catch (e: Exception) {
                Log.e("PokemonViewModel", "Error fetching Pokemon list", e)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun toggleFavorite(pokemon: Pokemon) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (pokemon.isFavorite) {
                    pokemonDao.deleteByName(pokemon.name)
                } else {
                    pokemonDao.insert(pokemon)
                }
                pokemon.isFavorite = !pokemon.isFavorite

                val currentList = _pokemonList.value
                if (currentList != null) {
                    val updatedList = currentList.map {
                        if (it.name == pokemon.name) {
                            pokemon
                        } else {
                            it
                        }
                    }.toMutableList()
                    _pokemonList.postValue(updatedList)
                }
            }
        }
    }
}
