package com.orlovandrei.ecosystem.entity;

import lombok.ToString;

@ToString(callSuper = true)
public class Plant extends Species {
    public Plant(String name) {
        super(name, "plant");
    }
}
