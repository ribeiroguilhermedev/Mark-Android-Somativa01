package com.example.pokedex.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Pokemon(
    @PrimaryKey val name: String,
    val type: String,
    val height: Int,
    val weight: Int,
    val imageUrl: String,
    var isFavorite: Boolean = false
)
