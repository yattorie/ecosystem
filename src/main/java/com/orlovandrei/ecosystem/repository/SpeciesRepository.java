package com.orlovandrei.ecosystem.repository;

import com.orlovandrei.ecosystem.entity.Animal;
import com.orlovandrei.ecosystem.entity.Plant;

public interface SpeciesRepository {
    void addPlant(String ecosystemName, Plant plant);

    void addAnimal(String ecosystemName, Animal animal);

    void deleteSpecies(String ecosystemName, String speciesName, boolean isPlant);

    void updateAnimalDiet(String ecosystemName, String animalName, String newDietType);

    boolean checkIfHerbivore(String ecosystemName, String animalName);

    boolean checkIfCarnivore(String ecosystemName, String animalName);

    boolean checkIfPlant(String ecosystemName, String speciesName);

    void recordInteraction(String interaction, String ecosystemName);

    boolean checkIfOmnivore(String ecosystemName, String predator);
}
