package com.orlovandrei.ecosystem.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Animal extends Species {
    String dietType;

    public Animal(String name, String dietType) {
        super(name, "animal");
        this.dietType = dietType;
    }
}
