package com.mobiquity.implementation.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item implements Serializable {

    @EqualsAndHashCode.Include
    private final int id;
    private final double weight;
    private final double value;

    public Item(int id, double weight, double value) {

        if (weight < 0 || weight > 100) {
            throw new IllegalArgumentException("Item weight must be non null in a range between 0 and 100.");
        }

        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Item value must be non null in a range between 0 and 100.");
        }

        this.id = id;
        this.weight = weight;
        this.value = value;
    }
}
