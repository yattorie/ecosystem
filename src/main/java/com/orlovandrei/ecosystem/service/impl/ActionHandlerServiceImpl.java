package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.entity.Animal;
import com.orlovandrei.ecosystem.entity.Conditions;
import com.orlovandrei.ecosystem.entity.Plant;
import com.orlovandrei.ecosystem.repository.SpeciesRepository;
import com.orlovandrei.ecosystem.repository.impl.SpeciesRepositoryImpl;
import com.orlovandrei.ecosystem.service.*;
import com.orlovandrei.ecosystem.util.Messages;

import java.util.Map;

@Service
public class ActionHandlerServiceImpl implements ActionHandlerService {
    private static ActionHandlerServiceImpl instance;

    private final SpeciesRepository speciesRepository = SpeciesRepositoryImpl.getInstance();
    private final SimulationService simulationService = SimulationServiceImpl.getInstance();
    private final InteractionService interactionService = InteractionServiceImpl.getInstance();
    private final PredictionService predictionService = PredictionServiceImpl.getInstance();
    private final UIService uiService = UIServiceImpl.getInstance();

    private ActionHandlerServiceImpl() {
    }

    public static synchronized ActionHandlerServiceImpl getInstance() {
        if (instance == null) {
            instance = new ActionHandlerServiceImpl();
        }
        return instance;
    }

    @Override
    public void addPlant(String ecosystemName) {
        String plantName = uiService.askForPlantName();
        Plant plant = new Plant(plantName);
        speciesRepository.addPlant(ecosystemName, plant);
    }

    @Override
    public void addAnimal(String ecosystemName) {
        Animal animal = uiService.askForAnimalDetails();
        speciesRepository.addAnimal(ecosystemName, animal);
    }

    @Override
    public void updateAnimalDiet(String ecosystemName) {
        String animalName = uiService.askForAnimalNameToUpdate();
        String newDiet = uiService.askForNewDiet();
        speciesRepository.updateAnimalDiet(ecosystemName, animalName, newDiet);
    }

    @Override
    public void deleteSpecies(String ecosystemName) {
        boolean isPlant = uiService.askIsPlant();
        String speciesName = uiService.askForSpeciesName(isPlant);
        speciesRepository.deleteSpecies(ecosystemName, speciesName, isPlant);
    }

    @Override
    public void handleInteraction(String ecosystemName) {
        interactionService.handleInteraction(ecosystemName);
    }

    @Override
    public void displayPopulationPredictions(String ecosystemName) {
        Conditions conditions = simulationService.getCurrentConditions(ecosystemName);
        Map<String, String> predictions = predictionService.predictPopulationChanges(conditions);
        predictions.forEach((species, prediction) -> {
            uiService.displayMessage(species + " " + Messages.POPULATION.getMessage() + ": " + prediction);
        });
    }
}
