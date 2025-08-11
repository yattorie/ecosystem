package com.orlovandrei.ecosystem.service;

import com.orlovandrei.ecosystem.entity.Conditions;

public interface SimulationService {
    Conditions getCurrentConditions(String ecosystemName);

    String createEcosystem();

    String loadEcosystem();
}
