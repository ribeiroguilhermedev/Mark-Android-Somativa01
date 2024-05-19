package com.example.pokedex.api

data class PokemonDetails(
    val name: String,
    val types: List<PokemonType>,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites,
    val abilities: List<PokemonAbility>,
    val stats: List<PokemonStat>
)

data class PokemonType(val type: TypeDetail)
data class TypeDetail(val name: String)
data class PokemonSprites(val front_default: String)
data class PokemonAbility(val ability: AbilityDetail)
data class AbilityDetail(val name: String)
data class PokemonStat(val stat: StatDetail, val base_stat: Int)
data class StatDetail(val name: String)
