package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.entity.Animal;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.util.EcosystemScanner;
import com.orlovandrei.ecosystem.util.Messages;

@Service
public class UIServiceImpl implements UIService {
    private static UIServiceImpl instance;

    private final EcosystemScanner ecosystemScanner = EcosystemScanner.getInstance();

    private UIServiceImpl() {
    }

    public static synchronized UIServiceImpl getInstance() {
        if (instance == null) {
            instance = new UIServiceImpl();
        }
        return instance;
    }

    @Override
    public int showMainMenu() {
        System.out.println(Messages.MENU.getMessage() + ":");
        System.out.println("1. " + Messages.CREATE_NEW_ECOSYSTEM.getMessage());
        System.out.println("2. " + Messages.LOAD_EXISTING_ECOSYSTEM.getMessage());
        System.out.println("3. " + Messages.EXIT_PROGRAM.getMessage());
        System.out.print(Messages.CHOOSE_AN_OPERATION.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextInt();
    }

    @Override
    public String askForEcosystemName(boolean isNew) {
        if (isNew) {
            System.out.print(Messages.ENTER_NEW_ECOSYSTEM_NAME.getMessage() + ": ");
        } else {
            System.out.print(Messages.ENTER_EXISTING_ECOSYSTEM_NAME.getMessage() + ": ");
        }
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public int showActionMenu() {
        System.out.println(Messages.MANAGING_ECOSYSTEM.getMessage() + ": ");
        System.out.println("1. " + Messages.ADD_PLANT.getMessage());
        System.out.println("2. " + Messages.ADD_ANIMAL.getMessage());
        System.out.println("3. " + Messages.EXIT_TO_MAIN_MENU.getMessage());
        System.out.println("4. " + Messages.UPDATE_ANIMAL_DIET.getMessage());
        System.out.println("5. " + Messages.DELETE_SPECIES.getMessage());
        System.out.println("6. " + Messages.INTERACTION_BETWEEN_SPECIES.getMessage());
        System.out.println("7. " + Messages.PREDICTION.getMessage());
        System.out.print(Messages.CHOOSE_AN_OPERATION.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextInt();
    }


    @Override
    public String askForPlantName() {
        ecosystemScanner.getScanner().nextLine();
        String plantName;


        while (true) {
            System.out.print(Messages.ENTER_PLANT_NAME.getMessage() + ": ");
            plantName = ecosystemScanner.getScanner().nextLine();
            if (plantName.matches("[a-zA-Zа-яА-Я]+")) {
                break;
            } else {
                System.out.println(Messages.INCORRECT_NAME.getMessage());
            }
        }
        return plantName;
    }

    @Override
    public Animal askForAnimalDetails() {
        ecosystemScanner.getScanner().nextLine();
        String animalName;
        String dietType;

        while (true) {
            System.out.print(Messages.ENTER_ANIMAL_NAME.getMessage() + ": ");
            animalName = ecosystemScanner.getScanner().nextLine();
            if (animalName.matches("[a-zA-Zа-яА-Я]+")) {
                break;
            } else {
                System.out.println(Messages.INCORRECT_NAME.getMessage());
            }
        }
        while (true) {
            System.out.print(Messages.ENTER_DIET_TYPE.getMessage() + ": ");
            dietType = ecosystemScanner.getScanner().nextLine();
            if (dietType.matches("herbivore|carnivore|omnivore")) {
                break;
            } else {
                System.out.println(Messages.INCORRECT_TYPE_OF_DIET.getMessage());
            }
        }
        return new Animal(animalName, dietType);
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String askForSpeciesName(boolean isPlant) {
        if (isPlant) {
            System.out.print(Messages.ENTER_PLANT_TO_REMOVE.getMessage() + ": ");
        } else {
            System.out.print(Messages.ENTER_ANIMAL_TO_REMOVE.getMessage() + ": ");
        }
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForNewDiet() {
        System.out.print(Messages.ENTER_NEW_DIET.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForAnimalNameToUpdate() {
        System.out.print(Messages.ENTER_ANIMAL_NAME_TO_UPDATE.getMessage() + ": ");
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public boolean askIsPlant() {
        System.out.println(Messages.DELETE_WHAT.getMessage());
        System.out.println("1. " + Messages.PLANT.getMessage());
        System.out.println("2. " + Messages.ANIMAL.getMessage());
        System.out.print(Messages.CHOOSE_AN_OPERATION.getMessage() + ": ");
        int choice = ecosystemScanner.getScanner().nextInt();
        return choice == 1;
    }

    @Override
    public String askForPredator() {
        System.out.print(Messages.ENTER_PREDATOR_NAME.getMessage() + ": ");
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForPrey() {
        System.out.print(Messages.ENTER_PREY_NAME.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public double askForTemperature() {
        System.out.print(Messages.ENTER_TEMPERATURE.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }

    @Override
    public double askForHumidity() {
        System.out.print(Messages.ENTER_HUMIDITY.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }

    @Override
    public double askForAvailableWater() {
        System.out.print(Messages.ENTER_AVAILABLE_WATER.getMessage() + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }
}
