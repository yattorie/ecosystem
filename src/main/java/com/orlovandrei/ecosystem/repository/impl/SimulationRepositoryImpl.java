package com.orlovandrei.ecosystem.repository.impl;

import com.orlovandrei.ecosystem.annotation.Repository;
import com.orlovandrei.ecosystem.exception.EcosystemCreationException;
import com.orlovandrei.ecosystem.exception.FileCreationException;
import com.orlovandrei.ecosystem.exception.FileReadException;
import com.orlovandrei.ecosystem.exception.FileWriteException;
import com.orlovandrei.ecosystem.entity.Conditions;
import com.orlovandrei.ecosystem.repository.SimulationRepository;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.service.impl.UIServiceImpl;
import com.orlovandrei.ecosystem.util.Config;
import com.orlovandrei.ecosystem.util.Messages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
            throw new EcosystemCreationException(Messages.ERROR_CREATING_ECOSYSTEM.getMessage() + ": " + e.getMessage(), e);
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
            throw new FileCreationException(Messages.ERROR_CREATING_FILE.getMessage() + " " + filePath.getFileName() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void loadSimulation(String ecosystemName) {
        displayFileContent(ecosystemName, Config.getProperty("plants.file"), Messages.PLANTS_IN_THE_ECOSYSTEM.getMessage());
        displayFileContent(ecosystemName, Config.getProperty("animals.file"), Messages.ANIMALS_IN_THE_ECOSYSTEM.getMessage());
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
            throw new FileReadException(Messages.ERROR_WHILE_READING_A_FILE.getMessage() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void saveEcosystemParameters(String ecosystemName, Conditions conditions) {
        Path resourceFilePath = Paths.get(getEcosystemDirectory(ecosystemName), Config.getProperty("resource.file"));
        writeConditionsToFile(resourceFilePath, conditions);
    }

    private void writeConditionsToFile(Path filePath, Conditions conditions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write(Messages.TEMPERATURE.getMessage() + ": " + conditions.getTemperature() + "\n");
            writer.write(Messages.HUMIDITY.getMessage() + ": " + conditions.getHumidity() + "\n");
            writer.write(Messages.AVAILABLE_WATER.getMessage() + ": " + conditions.getWaterAmount() + "\n");
        } catch (IOException e) {
            throw new FileWriteException(Messages.ERROR_WHEN_RECORDING_PARAMETERS.getMessage() + ": " + e.getMessage(), e);
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
                if (line.startsWith(Messages.TEMPERATURE.getMessage())) {
                    temperature = parseParameter(line);
                } else if (line.startsWith(Messages.HUMIDITY.getMessage())) {
                    humidity = parseParameter(line);
                } else if (line.startsWith(Messages.AVAILABLE_WATER.getMessage())) {
                    waterAmount = parseParameter(line);
                }
            }
        } catch (IOException e) {
            throw new FileReadException(Messages.ERROR_WHILE_READING_A_FILE.getMessage() + ": " + e.getMessage(), e);
        }

        return new Conditions(temperature, humidity, waterAmount);
    }

    private double parseParameter(String line) {
        return Double.parseDouble(line.split(": ")[1]);
    }
}
