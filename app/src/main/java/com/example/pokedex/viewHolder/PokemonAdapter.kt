package com.example.pokedex.viewHolder

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import com.example.pokedex.view.DetailsActivity
import com.squareup.picasso.Picasso

class PokemonAdapter(
    private val pokemonList: MutableList<Pokemon>,
    private val onFavoriteClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pokemonImage: ImageView = view.findViewById(R.id.pokemon_image)
        val pokemonName: TextView = view.findViewById(R.id.pokemon_name)
        val pokemonType: TextView = view.findViewById(R.id.pokemon_type)
        val pokemonHeight: TextView = view.findViewById(R.id.pokemon_height)
        val pokemonWeight: TextView = view.findViewById(R.id.pokemon_weight)
        val favoriteIcon: ImageView = view.findViewById(R.id.favorite_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text = pokemon.name
        holder.pokemonType.text = "Type: ${pokemon.type}"
        holder.pokemonHeight.text = "Height: ${pokemon.height}m"
        holder.pokemonWeight.text = "Weight: ${pokemon.weight}kg"
        Picasso.get()
            .load(pokemon.imageUrl)
            .into(holder.pokemonImage)

        holder.favoriteIcon.setImageResource(
            if (pokemon.isFavorite) R.drawable.baseline_favorite_24
            else R.drawable.baseline_favorite_border_24
        )

        holder.favoriteIcon.setOnClickListener {
            onFavoriteClick(pokemon)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailsActivity::class.java).apply {
                putExtra("POKEMON_NAME", pokemon.name)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = pokemonList.size

}
