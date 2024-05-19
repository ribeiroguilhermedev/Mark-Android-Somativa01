package com.example.pokedex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.viewHolder.PokemonAdapter
import com.example.pokedex.viewModel.FavoritesViewModel

class FavoritosFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favoritos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFavorites)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBarFavorites)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.favoritePokemonList.observe(viewLifecycleOwner, Observer { favoritePokemonList ->
            val adapter = PokemonAdapter(favoritePokemonList) { pokemon ->
                viewModel.toggleFavorite(pokemon)
            }
            recyclerView.adapter = adapter
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchFavoritePokemon()
    }
}
