package com.orlovandrei.ecosystem.repository;

import com.orlovandrei.ecosystem.entity.Conditions;

public interface SimulationRepository {

    void createNewSimulation(String ecosystemName);

    void loadSimulation(String ecosystemName);

    void saveEcosystemParameters(String ecosystemName, Conditions conditions);

    Conditions readEcosystemConditions(String ecosystemName);

    boolean ecosystemExists(String ecosystemName);
}
