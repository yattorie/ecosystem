package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.repository.SpeciesRepository;
import com.orlovandrei.ecosystem.repository.impl.SpeciesRepositoryImpl;
import com.orlovandrei.ecosystem.service.InteractionService;
import com.orlovandrei.ecosystem.service.UIService;

import static com.orlovandrei.ecosystem.util.Messages.ATE;
import static com.orlovandrei.ecosystem.util.Messages.ATE_A_HERBIVORE;
import static com.orlovandrei.ecosystem.util.Messages.ATE_A_PLANT;
import static com.orlovandrei.ecosystem.util.Messages.CANT_EAT;
import static com.orlovandrei.ecosystem.util.Messages.HERBIVORE;
import static com.orlovandrei.ecosystem.util.Messages.INTERACTION_IS_NOT_POSSIBLE;
import static com.orlovandrei.ecosystem.util.Messages.OMNIVORE;
import static com.orlovandrei.ecosystem.util.Messages.PREDATOR;

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
            uiService.displayMessage(INTERACTION_IS_NOT_POSSIBLE + ": " + predatorName + " " + CANT_EAT + " " + preyName);
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
        uiService.displayMessage(HERBIVORE + " " + predator + " " + ATE_A_PLANT + " " + prey);
        speciesRepository.recordInteraction(predator + " " + ATE + " " + prey, ecosystemName);
    }

    private void performCarnivoreEatsHerbivore(String ecosystemName, String predator, String prey) {
        speciesRepository.deleteSpecies(ecosystemName, prey, false);
        uiService.displayMessage(PREDATOR + " " + predator + " " + ATE_A_HERBIVORE + " " + prey);
        speciesRepository.recordInteraction(predator + " " + ATE + " " + prey, ecosystemName);
    }

    private void performOmnivoreEats(String ecosystemName, String predator, String prey) {
        boolean isPlant = speciesRepository.checkIfPlant(ecosystemName, prey);
        speciesRepository.deleteSpecies(ecosystemName, prey, isPlant);

        uiService.displayMessage(OMNIVORE + " " + predator + " " + ATE + " " + prey);

        speciesRepository.recordInteraction(predator + " " + ATE + " " + prey, ecosystemName);
    }
}
