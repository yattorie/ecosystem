package com.orlovandrei.ecosystem.repository.impl;

import com.orlovandrei.ecosystem.annotation.Repository;
import com.orlovandrei.ecosystem.exception.EcosystemCreationException;
import com.orlovandrei.ecosystem.exception.FileCreationException;
import com.orlovandrei.ecosystem.exception.FileReadException;
import com.orlovandrei.ecosystem.exception.FileWriteException;
import com.orlovandrei.ecosystem.model.Conditions;
import com.orlovandrei.ecosystem.repository.SimulationRepository;
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

import static com.orlovandrei.ecosystem.util.Messages.ANIMALS_IN_THE_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.AVAILABLE_WATER;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_CREATING_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_CREATING_FILE;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_WHEN_RECORDING_PARAMETRS;
import static com.orlovandrei.ecosystem.util.Messages.ERROR_WHILE_READING_A_FILE;
import static com.orlovandrei.ecosystem.util.Messages.HUMIDITY;
import static com.orlovandrei.ecosystem.util.Messages.PLANTS_IN_THE_ECOSYSTEM;
import static com.orlovandrei.ecosystem.util.Messages.TEMPERATURE;

@Repository
public class SimulationRepositoryImpl implements SimulationRepository {
    private static SimulationRepositoryImpl instance;
    private final UIService uiService = UIServiceImpl.getInstance();

    private SimulationRepositoryImpl() {
    }

    public static synchronized SimulationRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new SimulationRepositoryImpl();
        }
        return instance;
    }

    private String getEcosystemDirectory(String ecosystemName) {
        return Config.getDirectory() + ecosystemName;
    }

    @Override
    public void createNewSimulation(String ecosystemName) {
        Path ecosystemDirPath = Paths.get(getEcosystemDirectory(ecosystemName));
        createDirectoryIfNotExists(ecosystemDirPath);
        createSimulationFiles(ecosystemDirPath);
    }

    private void createDirectoryIfNotExists(Path ecosystemDirPath) {
        try {
            if (!Files.exists(ecosystemDirPath)) {
                Files.createDirectories(ecosystemDirPath);
            }
        } catch (IOException e) {
            throw new EcosystemCreationException(ERROR_CREATING_ECOSYSTEM + ": " + e.getMessage(), e);
        }
    }

    private void createSimulationFiles(Path dirPath) {
        List<String> files = List.of(
                Config.getProperty("plants.file"),
                Config.getProperty("animals.file"),
                Config.getProperty("interactions.file"),
                Config.getProperty("resource.file")
        );
        for (String file : files) {
            createFileIfNotExists(dirPath.resolve(file));
        }
    }

    private void createFileIfNotExists(Path filePath) {
        try {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new FileCreationException(ERROR_CREATING_FILE + " " + filePath.getFileName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void loadSimulation(String ecosystemName) {
        displayFileContent(ecosystemName, Config.getProperty("plants.file"), PLANTS_IN_THE_ECOSYSTEM);
        displayFileContent(ecosystemName, Config.getProperty("animals.file"), ANIMALS_IN_THE_ECOSYSTEM);
    }

    private void displayFileContent(String ecosystemName, String fileName, String headerMessage) {
        Path filePath = Paths.get(getEcosystemDirectory(ecosystemName), fileName);
        uiService.displayMessage(headerMessage + ecosystemName + ": ");
        readFileContent(filePath);
    }

    private void readFileContent(Path filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                uiService.displayMessage(line);
            }
        } catch (IOException e) {
            throw new FileReadException(ERROR_WHILE_READING_A_FILE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void saveEcosystemParameters(String ecosystemName, Conditions conditions) {
        Path resourceFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("resource.file"));
        writeConditionsToFile(resourceFilePath, conditions);
    }

    private void writeConditionsToFile(Path filePath, Conditions conditions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(TEMPERATURE + ": " + conditions.getTemperature() + "\n");
            writer.write(HUMIDITY + ": " + conditions.getHumidity() + "\n");
            writer.write(AVAILABLE_WATER + ": " + conditions.getWaterAmount() + "\n");
        } catch (IOException e) {
            throw new FileWriteException(ERROR_WHEN_RECORDING_PARAMETRS + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Conditions readEcosystemConditions(String ecosystemName) {
        Path resourceFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("resource.file"));
        return parseConditionsFromFile(resourceFilePath);
    }

    @Override
    public boolean ecosystemExists(String ecosystemName) {
        Path ecosystemDirPath = Paths.get(getEcosystemDirectory(ecosystemName));
        return Files.exists(ecosystemDirPath);
    }

    private Conditions parseConditionsFromFile(Path filePath) {
        double temperature = 0.0;
        double humidity = 0.0;
        double waterAmount = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(TEMPERATURE)) {
                    temperature = parseParameter(line);
                } else if (line.startsWith(HUMIDITY)) {
                    humidity = parseParameter(line);
                } else if (line.startsWith(AVAILABLE_WATER)) {
                    waterAmount = parseParameter(line);
                }
            }
        } catch (IOException e) {
            throw new FileReadException(ERROR_WHILE_READING_A_FILE + ": " + e.getMessage(), e);
        }

        return new Conditions(temperature, humidity, waterAmount);
    }

    private double parseParameter(String line) {
        return Double.parseDouble(line.split(": ")[1]);
    }
}
