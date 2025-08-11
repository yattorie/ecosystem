package com.orlovandrei.ecosystem.service.impl;

import com.orlovandrei.ecosystem.annotation.Service;
import com.orlovandrei.ecosystem.entity.Conditions;
import com.orlovandrei.ecosystem.service.PredictionService;

import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionServiceImpl implements PredictionService {
    private static PredictionServiceImpl instance;

    private PredictionServiceImpl() {
    }

    public static synchronized PredictionServiceImpl getInstance() {
        if (instance == null) {
            instance = new PredictionServiceImpl();
        }
        return instance;
    }

    @Override
    public Map<String, String> predictPopulationChanges(Conditions conditions) {
        Map<String, String> predictions = new HashMap<>();

        predictions.put("Plants", predictPlantsChange(conditions));
        predictions.put("Animals", predictAnimalsChange(conditions));

        return predictions;
    }

    private String predictPlantsChange(Conditions conditions) {
        double temperature = conditions.getTemperature();
        double humidity = conditions.getHumidity();
        double waterAmount = conditions.getWaterAmount();

        if (temperature > 35 && humidity < 30) {
            return "Significant Decrease";
        } else if (temperature > 30 && waterAmount < 20) {
            return "Decrease";
        } else if (temperature < 10 || humidity < 15) {
            return "Stable";
        } else if (temperature > 15 && temperature <= 25 && humidity >= 50) {
            return "Increase";
        } else {
            return "Stable";
        }
    }

    private String predictAnimalsChange(Conditions conditions) {
        double temperature = conditions.getTemperature();
        double humidity = conditions.getHumidity();
        double waterAmount = conditions.getWaterAmount();

        if (temperature > 35 && waterAmount < 30) {
            return "Significant Decrease";
        } else if (temperature > 30 && humidity < 40) {
            return "Decrease";
        } else if (temperature < 10 && waterAmount > 40) {
            return "Stable";
        } else if (temperature >= 20 && temperature <= 30 && humidity > 60 && waterAmount > 50) {
            return "Increase";
        } else {
            return "Stable";
        }
    }
}
