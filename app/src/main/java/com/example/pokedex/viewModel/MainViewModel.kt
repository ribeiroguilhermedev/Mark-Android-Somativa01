package com.example.pokedex.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val exampleData = MutableLiveData<String>()

    fun loadExampleData() {
        exampleData.value = "Dados Carregados"
    }
}
