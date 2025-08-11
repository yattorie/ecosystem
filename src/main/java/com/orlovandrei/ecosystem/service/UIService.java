package com.orlovandrei.ecosystem.service;

import com.orlovandrei.ecosystem.entity.Animal;

public interface UIService {
    int showMainMenu();

    String askForEcosystemName(boolean isNew);

    int showActionMenu();

    String askForPlantName();

    Animal askForAnimalDetails();

    void displayMessage(String message);

    String askForSpeciesName(boolean isPlant);

    String askForNewDiet();

    String askForAnimalNameToUpdate();

    boolean askIsPlant();

    String askForPredator();

    String askForPrey();

    double askForTemperature();

    double askForHumidity();

    double askForAvailableWater();
}
