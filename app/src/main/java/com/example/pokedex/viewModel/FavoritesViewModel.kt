package com.example.pokedex.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.PokemonDatabase
import com.example.pokedex.model.Pokemon
import com.example.pokedex.util.AppState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val pokemonDao = PokemonDatabase.getDatabase(application).pokemonDao()

    private val _favoritePokemonList = MutableLiveData<MutableList<Pokemon>>()
    val favoritePokemonList: LiveData<MutableList<Pokemon>> get() = _favoritePokemonList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        fetchFavoritePokemon()
    }

    fun fetchFavoritePokemon() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val favoritePokemon = withContext(Dispatchers.IO) {
                    pokemonDao.getAllFavorites().toMutableList()
                }

                for (pokemon in favoritePokemon) {
                    pokemon.isFavorite = true
                }

                _favoritePokemonList.postValue(favoritePokemon)
            } catch (e: Exception) {
                // Handle error
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
                AppState.isFavoritesUpdated = true

                // Update only the modified item in the list
                val currentList = _favoritePokemonList.value
                if (currentList != null) {
                    val updatedList = currentList.map {
                        if (it.name == pokemon.name) {
                            pokemon
                        } else {
                            it
                        }
                    }.toMutableList()
                    _favoritePokemonList.postValue(updatedList)
                }
            }
        }
    }
}
