package com.orlovandrei.ecosystem.repository.impl;

import com.orlovandrei.ecosystem.annotation.Repository;
import com.orlovandrei.ecosystem.exception.FileCreationException;
import com.orlovandrei.ecosystem.exception.FileReadException;
import com.orlovandrei.ecosystem.exception.FileWriteException;
import com.orlovandrei.ecosystem.model.Animal;
import com.orlovandrei.ecosystem.model.Plant;
import com.orlovandrei.ecosystem.repository.SpeciesRepository;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.service.impl.UIServiceImpl;
import com.orlovandrei.ecosystem.util.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static com.orlovandrei.ecosystem.util.Messages.ADDED_TO_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.ANIMAL_DIET;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_ADDING_SPECIES;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_CHECKING_ANIMAL_DIET;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_CREATING_FILE;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_RECORDING_INTERACTION;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_UPDATING_ANIMAL_DIET;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_WHEN_CHECKING_A_PLANT;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_WHEN_DELETING_A_SPECIE;
import static com.orlovandrei.ecosystem.util.Messages.INTERACTION_RECORDED;
import static com.orlovandrei.ecosystem.util.Messages.REMOVED_FROM_THE_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.SPECIE;
import static com.orlovandrei.ecosystem.util.Messages.UPDATED_TO;

@Repository
public class SpeciesRepositoryImpl implements SpeciesRepository {
    private static SpeciesRepositoryImpl instance;
    private final UIService uiService = UIServiceImpl.getInstance();

    private SpeciesRepositoryImpl() {
    }

    public static synchronized SpeciesRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new SpeciesRepositoryImpl();
        }
        return instance;
    }

    private String getEcosystemDirectory(String ecosystemName) {
        return Config.getDirectory() + ecosystemName;
    }

    @Override
    public void addPlant(String ecosystemName, Plant plant) {
        addSpecies(ecosystemName, plant.getName(), Config.getProperty("plants.file"));
    }

    @Override
    public void addAnimal(String ecosystemName, Animal animal) {
        addSpecies(ecosystemName, animal.getName() + " (" + animal.getDietType() + ")", Config.getProperty("animals.file"));
    }

    private void addSpecies(String ecosystemName, String speciesName, String fileName) {
        Path filePath = Paths.get(getEcosystemDirectory(ecosystemName), fileName);
        createFileIfNotExists(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), true))) {
            writer.write(speciesName);
            writer.newLine();
            uiService.displayMessage(speciesName + " " + ADDED_TO_ECOSYSTEM + " " + ecosystemName);
        } catch (IOException e) {
            throw new FileWriteException(ERROR_ADDING_SPECIES + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteSpecies(String ecosystemName, String speciesName, boolean isPlant) {
        Path filePath = Paths.get(getEcosystemDirectory(ecosystemName), isPlant ? Config.getProperty("plants.file") : Config.getProperty("animals.file"));

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            List<String> updatedLines = reader.lines()
                    .filter(line -> !(line.equals(speciesName) || line.startsWith(speciesName + " (")))
                    .collect(Collectors.toList());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile(), false))) {
                for (String line : updatedLines) {
                    writer.write(line);
                    writer.newLine();
                }
                uiService.displayMessage(SPECIE + " " + speciesName + " " + REMOVED_FROM_THE_ECOSYSTEM + " " + ecosystemName);
            }
        } catch (IOException e) {
            throw new FileWriteException(ERROR_WHEN_DELETING_A_SPECIE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void updateAnimalDiet(String ecosystemName, String animalName, String newDietType) {
        Path animalsFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("animals.file"));

        try (BufferedReader reader = new BufferedReader(new FileReader(animalsFilePath.toFile()))) {
            List<String> updatedLines = reader.lines()
                    .map(line -> line.startsWith(animalName + " (") ? animalName + " (" + newDietType + ")" : line)
                    .collect(Collectors.toList());

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(animalsFilePath.toFile(), false))) {
                for (String line : updatedLines) {
                    writer.write(line);
                    writer.newLine();
                }
                uiService.displayMessage(ANIMAL_DIET + " " + animalName + " " + UPDATED_TO + " " + newDietType);
            }
        } catch (IOException e) {
            throw new FileWriteException(ERROR_UPDATING_ANIMAL_DIET + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkIfHerbivore(String ecosystemName, String animalName) {
        return checkAnimalDiet(ecosystemName, animalName, "herbivore");
    }

    @Override
    public boolean checkIfCarnivore(String ecosystemName, String animalName) {
        return checkAnimalDiet(ecosystemName, animalName, "carnivore");
    }

    @Override
    public boolean checkIfOmnivore(String ecosystemName, String speciesName) {
        return checkAnimalDiet(ecosystemName, speciesName, "omnivore");
    }

    private boolean checkAnimalDiet(String ecosystemName, String animalName, String dietType) {
        Path animalsFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("animals.file"));

        try (BufferedReader reader = new BufferedReader(new FileReader(animalsFilePath.toFile()))) {
            return reader.lines()
                    .anyMatch(line -> line.startsWith(animalName) && line.contains(dietType));
        } catch (IOException e) {
            throw new FileReadException(ERROR_CHECKING_ANIMAL_DIET + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean checkIfPlant(String ecosystemName, String speciesName) {
        Path plantsFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("plants.file"));

        try (BufferedReader reader = new BufferedReader(new FileReader(plantsFilePath.toFile()))) {
            return reader.lines().anyMatch(line -> line.contains(speciesName));
        } catch (IOException e) {
            throw new FileReadException(ERROR_WHEN_CHECKING_A_PLANT + ": " + e.getMessage(), e);
        }
    }

    private void createFileIfNotExists(Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new FileCreationException(ERROR_CREATING_FILE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void recordInteraction(String interaction, String ecosystemName) {
        Path interactionsFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("interactions.file"));
        createFileIfNotExists(interactionsFilePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(interactionsFilePath.toFile(), true))) {
            writer.write(interaction);
            writer.newLine();
            System.out.println(INTERACTION_RECORDED + ": " + interaction);
        } catch (IOException e) {
            throw new FileWriteException(ERROR_RECORDING_INTERACTION + ": " + e.getMessage(), e);
        }
    }
}
