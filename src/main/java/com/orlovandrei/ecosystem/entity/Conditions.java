package com.orlovandrei.ecosystem.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conditions {
    double temperature;
    double humidity;
    double waterAmount;
}
