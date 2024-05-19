package com.example.pokedex.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.pokedex.R
import com.example.pokedex.api.PokemonDetails
import com.example.pokedex.api.RetrofitInstance
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val pokemonName = intent.getStringExtra("POKEMON_NAME")

        if (pokemonName != null) {
            fetchPokemonDetails(pokemonName)
        }
    }

    private fun fetchPokemonDetails(pokemonName: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    RetrofitInstance.api.getPokemonDetails(pokemonName).execute()
                }
                if (response.isSuccessful) {
                    val pokemonDetails = response.body()
                    pokemonDetails?.let { updateUI(it) }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun updateUI(details: PokemonDetails) {
        val pokemonImage: ImageView = findViewById(R.id.pokemon_image)
        val pokemonName: TextView = findViewById(R.id.pokemon_name)
        val pokemonType: TextView = findViewById(R.id.pokemon_type)
        val pokemonHeight: TextView = findViewById(R.id.pokemon_height)
        val pokemonWeight: TextView = findViewById(R.id.pokemon_weight)
        val pokemonAbilities: TextView = findViewById(R.id.pokemon_abilities)
        val pokemonStats: TextView = findViewById(R.id.pokemon_stats)

        pokemonName.text = details.name
        pokemonType.text = "Type: ${details.types.joinToString { it.type.name }}"
        pokemonHeight.text = "Height: ${details.height}m"
        pokemonWeight.text = "Weight: ${details.weight}kg"
        pokemonAbilities.text = "Abilities: ${details.abilities.joinToString { it.ability.name }}"
        pokemonStats.text = "Stats: ${details.stats.joinToString { "${it.stat.name}: ${it.base_stat}" }}"

        Picasso.get()
            .load(details.sprites.front_default)
            .into(pokemonImage)
    }
}
