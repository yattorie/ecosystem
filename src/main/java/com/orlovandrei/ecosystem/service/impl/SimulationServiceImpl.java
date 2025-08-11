package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.entity.Conditions;
import com.orlovandrei.ecosystem.repository.SimulationRepository;
import com.orlovandrei.ecosystem.repository.impl.SimulationRepositoryImpl;
import com.orlovandrei.ecosystem.service.SimulationService;
import com.orlovandrei.ecosystem.service.UIService;
import com.orlovandrei.ecosystem.util.Messages;

@Service
public class SimulationServiceImpl implements SimulationService {
    private static SimulationServiceImpl instance;

    private final SimulationRepository simulationRepository = SimulationRepositoryImpl.getInstance();
    private final UIService uiService = UIServiceImpl.getInstance();

    private SimulationServiceImpl() {
    }

    public static synchronized SimulationServiceImpl getInstance() {
        if (instance == null) {
            instance = new SimulationServiceImpl();
        }
        return instance;
    }

    @Override
    public Conditions getCurrentConditions(String ecosystemName) {
        return simulationRepository.readEcosystemConditions(ecosystemName);
    }

    @Override
    public String createEcosystem() {
        String ecosystemName = uiService.askForEcosystemName(true);

        if (simulationRepository.ecosystemExists(ecosystemName)) {
            uiService.displayMessage(Messages.THIS_ECOSYSTEM_ALREADY_EXISTS.getMessage());
            return null;
        }

        simulationRepository.createNewSimulation(ecosystemName);

        Conditions conditions = new Conditions(
                uiService.askForTemperature(),
                uiService.askForHumidity(),
                uiService.askForAvailableWater()
        );

        simulationRepository.saveEcosystemParameters(ecosystemName, conditions);
        uiService.displayMessage(Messages.ECOSYSTEM_CREATE.getMessage());
        return ecosystemName;
    }

    @Override
    public String loadEcosystem() {
        String ecosystemName = uiService.askForEcosystemName(false);
        simulationRepository.loadSimulation(ecosystemName);
        uiService.displayMessage(Messages.ECOSYSTEM_LOADED.getMessage());
        return ecosystemName;
    }

}
