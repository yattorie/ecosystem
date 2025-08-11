package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.repository.SpeciesRepository;
import com.orlovandrei.ecosystem.repository.impl.SpeciesRepositoryImpl;
import com.orlovandrei.ecosystem.service.InteractionService;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.util.Messages;

@Service
public class InteractionServiceImpl implements InteractionService {
    private static InteractionServiceImpl instance;

    private final UIService uiService = UIServiceImpl.getInstance();
    private final SpeciesRepository speciesRepository = SpeciesRepositoryImpl.getInstance();

    private InteractionServiceImpl() {
    }

    public static synchronized InteractionServiceImpl getInstance() {
        if (instance == null) {
            instance = new InteractionServiceImpl();
        }
        return instance;
    }

    @Override
    public void handleInteraction(String ecosystemName) {
        String predatorName = uiService.askForPredator();
        String preyName = uiService.askForPrey();

        if (canHerbivoreEatPlant(ecosystemName, predatorName, preyName)) {
            performHerbivoreEatsPlant(ecosystemName, predatorName, preyName);
        } else if (canCarnivoreEatHerbivore(ecosystemName, predatorName, preyName)) {
            performCarnivoreEatsHerbivore(ecosystemName, predatorName, preyName);
        } else if (canOmnivoreEat(ecosystemName, predatorName, preyName)) {
            performOmnivoreEats(ecosystemName, predatorName, preyName);
        } else {
            uiService.displayMessage(Messages.INTERACTION_IS_NOT_POSSIBLE.getMessage() + ": " + predatorName + " " + Messages.CANT_EAT.getMessage() + " " + preyName);
        }
    }

    private boolean canHerbivoreEatPlant(String ecosystemName, String predator, String prey) {
        return speciesRepository.checkIfHerbivore(ecosystemName, predator) && speciesRepository.checkIfPlant(ecosystemName, prey);
    }

    private boolean canCarnivoreEatHerbivore(String ecosystemName, String predator, String prey) {
        return speciesRepository.checkIfCarnivore(ecosystemName, predator) && speciesRepository.checkIfHerbivore(ecosystemName, prey);
    }

    private boolean canOmnivoreEat(String ecosystemName, String predator, String prey) {
        return speciesRepository.checkIfOmnivore(ecosystemName, predator) &&
                (speciesRepository.checkIfPlant(ecosystemName, prey) || speciesRepository.checkIfHerbivore(ecosystemName, prey) || speciesRepository.checkIfCarnivore(ecosystemName, prey));
    }

    private void performHerbivoreEatsPlant(String ecosystemName, String predator, String prey) {
        speciesRepository.deleteSpecies(ecosystemName, prey, true);
        uiService.displayMessage(Messages.HERBIVORE.getMessage() + " " + predator + " " + Messages.ATE_A_PLANT.getMessage() + " " + prey);
        speciesRepository.recordInteraction(predator + " " + Messages.ATE.getMessage() + " " + prey, ecosystemName);
    }

    private void performCarnivoreEatsHerbivore(String ecosystemName, String predator, String prey) {
        speciesRepository.deleteSpecies(ecosystemName, prey, false);
        uiService.displayMessage(Messages.PREDATOR.getMessage() + " " + predator + " " + Messages.ATE_A_HERBIVORE.getMessage() + " " + prey);
        speciesRepository.recordInteraction(predator + " " + Messages.ATE.getMessage() + " " + prey, ecosystemName);
    }

    private void performOmnivoreEats(String ecosystemName, String predator, String prey) {
        boolean isPlant = speciesRepository.checkIfPlant(ecosystemName, prey);
        speciesRepository.deleteSpecies(ecosystemName, prey, isPlant);

        uiService.displayMessage(Messages.OMNIVORE.getMessage() + " " + predator + " " + Messages.ATE.getMessage() + " " + prey);

        speciesRepository.recordInteraction(predator + " " + Messages.ATE.getMessage() + " " + prey, ecosystemName);
    }
}
