package com.example.pokedex.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.pokedex.model.Pokemon;

import java.util.List;

@Dao
public interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Pokemon pokemon);

    @Query("SELECT * FROM favorites")
    List<Pokemon> getAllFavorites();

    @Query("DELETE FROM favorites WHERE name = :name")
    void deleteByName(String name);

    @Query("SELECT * FROM favorites WHERE name = :name")
    Pokemon getPokemonByName(String name);
}
