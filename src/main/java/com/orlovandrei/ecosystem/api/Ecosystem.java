package com.orlovandrei.ecosystem.api;

import com.orlovandrei.ecosystem.service.ActionHandlerService;
import com.orlovandrei.ecosystem.service.SimulationService;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.service.impl.ActionHandlerServiceImpl;
import com.orlovandrei.ecosystem.service.impl.SimulationServiceImpl;
import com.orlovandrei.ecosystem.service.impl.UIServiceImpl;
import com.orlovandrei.ecosystem.util.Messages;

public class Ecosystem {

    private final SimulationService simulationService = SimulationServiceImpl.getInstance();
    private final UIService uiService = UIServiceImpl.getInstance();
    private final ActionHandlerService actionHandler = ActionHandlerServiceImpl.getInstance();

    public void startEcoSystem() {
        String ecosystemName = "";

        uiService.displayMessage(Messages.WELCOME_MESSAGE.getMessage());

        while (true) {
            int choice = uiService.showMainMenu();

            switch (choice) {
                case 1:
                    ecosystemName = simulationService.createEcosystem();
                    break;
                case 2:
                    ecosystemName = simulationService.loadEcosystem();
                    break;
                case 3:
                    uiService.displayMessage(Messages.EXIT_PROGRAM.getMessage());
                    return;
                default:
                    uiService.displayMessage(Messages.INCORRECT_SELECTION.getMessage());
            }
            if (ecosystemName != null && !ecosystemName.isEmpty()) {
                manageEcosystem(ecosystemName);
            } else {
                uiService.displayMessage(Messages.FAILED_TO_LOAD_OR_CREATE_ECOSYSTEM.getMessage());
            }
        }
    }

    private void manageEcosystem(String ecosystemName) {
        boolean continueManaging = true;

        while (continueManaging) {
            int actionChoice = uiService.showActionMenu();

            switch (actionChoice) {
                case 1:
                    actionHandler.addPlant(ecosystemName);
                    break;
                case 2:
                    actionHandler.addAnimal(ecosystemName);
                    break;
                case 3:
                    continueManaging = false;
                    break;
                case 4:
                    actionHandler.updateAnimalDiet(ecosystemName);
                    break;
                case 5:
                    actionHandler.deleteSpecies(ecosystemName);
                    break;
                case 6:
                    actionHandler.handleInteraction(ecosystemName);
                    break;
                case 7:
                    actionHandler.displayPopulationPredictions(ecosystemName);
                    break;
                default:
                    uiService.displayMessage(Messages.INCORRECT_SELECTION.getMessage());
            }
        }
    }
}
