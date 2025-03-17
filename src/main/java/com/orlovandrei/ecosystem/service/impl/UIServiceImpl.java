package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.model.Animal;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.util.EcosystemScanner;

import static com.orlovandrei.ecosystem.util.Messages.ADD_ANIMAL;
import static com.orlovandrei.ecosystem.util.Messages.ADD_PLANT;
import static com.orlovandrei.ecosystem.util.Messages.ANIMAL;
import static com.orlovandrei.ecosystem.util.Messages.CHOOSE_AN_OPERATION;
import static com.orlovandrei.ecosystem.util.Messages.CREATE_NEW_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.DELETE_SPECIES;
import static com.orlovandrei.ecosystem.util.Messages.DELETE_WHAT;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_ANIMAL_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_ANIMAL_NAME_TO_UPDATE;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_ANIMAL_TO_REMOVE;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_AVAILABLE_WATER;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_DIET_TYPE;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_EXISTING_ECOSYSTEM_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_HUMIDITY;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_NEW_DIET;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_NEW_ECOSYSTEM_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_PLANT_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_PLANT_TO_REMOVE;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_PREDATOR_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_PREY_NAME;
import static com.orlovandrei.ecosystem.util.Messages.ENTER_TEMPERATURE;
import static com.orlovandrei.ecosystem.util.Messages.EXIT_PROGRAM;
import static com.orlovandrei.ecosystem.util.Messages.EXIT_TO_MAIN_MENU;
import static com.orlovandrei.ecosystem.util.Messages.INCORECT_TYPE_OF_DIET;
import static com.orlovandrei.ecosystem.util.Messages.INCORRECT_NAME;
import static com.orlovandrei.ecosystem.util.Messages.INTERACTION_BETWEEN_SPECIES;
import static com.orlovandrei.ecosystem.util.Messages.LOAD_EXISTING_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.MANAGING_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.MENU;
import static com.orlovandrei.ecosystem.util.Messages.PLANT;
import static com.orlovandrei.ecosystem.util.Messages.PREDICTION;
import static com.orlovandrei.ecosystem.util.Messages.UPDATE_ANIMAL_DIET;

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
        System.out.println(MENU + ":");
        System.out.println("1. " + CREATE_NEW_ECOSYSTEM);
        System.out.println("2. " + LOAD_EXISTING_ECOSYSTEM);
        System.out.println("3. " + EXIT_PROGRAM);
        System.out.print(CHOOSE_AN_OPERATION + ": ");
        return ecosystemScanner.getScanner().nextInt();
    }

    @Override
    public String askForEcosystemName(boolean isNew) {
        if (isNew) {
            System.out.print(ENTER_NEW_ECOSYSTEM_NAME + ": ");
        } else {
            System.out.print(ENTER_EXISTING_ECOSYSTEM_NAME + ": ");
        }
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public int showActionMenu() {
        System.out.println(MANAGING_ECOSYSTEM + ": ");
        System.out.println("1. " + ADD_PLANT);
        System.out.println("2. " + ADD_ANIMAL);
        System.out.println("3. " + EXIT_TO_MAIN_MENU);
        System.out.println("4. " + UPDATE_ANIMAL_DIET);
        System.out.println("5. " + DELETE_SPECIES);
        System.out.println("6. " + INTERACTION_BETWEEN_SPECIES);
        System.out.println("7. " + PREDICTION);
        System.out.print(CHOOSE_AN_OPERATION + ": ");
        return ecosystemScanner.getScanner().nextInt();
    }


    @Override
    public String askForPlantName() {
        ecosystemScanner.getScanner().nextLine();
        String plantName;


        while (true) {
            System.out.print(ENTER_PLANT_NAME + ": ");
            plantName = ecosystemScanner.getScanner().nextLine();
            if (plantName.matches("[a-zA-Zа-яА-Я]+")) {
                break;
            } else {
                System.out.println(INCORRECT_NAME);
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
            System.out.print(ENTER_ANIMAL_NAME + ": ");
            animalName = ecosystemScanner.getScanner().nextLine();
            if (animalName.matches("[a-zA-Zа-яА-Я]+")) {
                break;
            } else {
                System.out.println(INCORRECT_NAME);
            }
        }
        while (true) {
            System.out.print(ENTER_DIET_TYPE + ": ");
            dietType = ecosystemScanner.getScanner().nextLine();
            if (dietType.matches("herbivore|carnivore|omnivore")) {
                break;
            } else {
                System.out.println(INCORECT_TYPE_OF_DIET);
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
            System.out.print(ENTER_PLANT_TO_REMOVE + ": ");
        } else {
            System.out.print(ENTER_ANIMAL_TO_REMOVE + ": ");
        }
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForNewDiet() {
        System.out.print(ENTER_NEW_DIET + ": ");
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForAnimalNameToUpdate() {
        System.out.print(ENTER_ANIMAL_NAME_TO_UPDATE + ": ");
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public boolean askIsPlant() {
        System.out.println(DELETE_WHAT);
        System.out.println("1. " + PLANT);
        System.out.println("2. " + ANIMAL);
        System.out.print(CHOOSE_AN_OPERATION + ": ");
        int choice = ecosystemScanner.getScanner().nextInt();
        return choice == 1;
    }

    @Override
    public String askForPredator() {
        System.out.print(ENTER_PREDATOR_NAME + ": ");
        ecosystemScanner.getScanner().nextLine();
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public String askForPrey() {
        System.out.print(ENTER_PREY_NAME + ": ");
        return ecosystemScanner.getScanner().nextLine();
    }

    @Override
    public double askForTemperature() {
        System.out.print(ENTER_TEMPERATURE + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }

    @Override
    public double askForHumidity() {
        System.out.print(ENTER_HUMIDITY + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }

    @Override
    public double askForAvailableWater() {
        System.out.print(ENTER_AVAILABLE_WATER + ": ");
        return ecosystemScanner.getScanner().nextDouble();
    }
}
