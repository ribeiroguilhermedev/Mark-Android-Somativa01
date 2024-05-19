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
import com.example.pokedex.util.AppState
import com.example.pokedex.viewHolder.PokemonAdapter
import com.example.pokedex.viewModel.PokemonViewModel

class ListaFragment : Fragment() {

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lista, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.pokemonList.observe(viewLifecycleOwner, Observer { pokemonList ->
            val adapter = PokemonAdapter(pokemonList) { pokemon ->
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
        if (AppState.isFavoritesUpdated) {
            viewModel.fetchPokemonList()
            AppState.isFavoritesUpdated = false
        }
    }
}
