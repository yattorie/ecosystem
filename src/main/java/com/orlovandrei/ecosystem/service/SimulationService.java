package com.orlovandrei.ecosystem.service;

import com.orlovandrei.ecosystem.model.Conditions;

public interface SimulationService {
    Conditions getCurrentConditions(String ecosystemName);

    String createEcosystem();

    String loadEcosystem();
}
