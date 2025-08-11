package com.orlovandrei.ecosystem.service;

import com.orlovandrei.ecosystem.entity.Conditions;

import java.util.Map;

public interface PredictionService {
    Map<String, String> predictPopulationChanges(Conditions conditions);
}
