package com.mobiquity.implementation.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public final class TestCase {

    private final double maxWeight;
    private final List<Item> items;

    public TestCase(double maxWeight, List<Item> items) {
        if (maxWeight < 0 || maxWeight > 100) {
            throw new IllegalArgumentException("Max weight must be in between a range of 0 and 100.");
        }

        if (items == null || items.isEmpty() || items.size() > 15) {
            throw new IllegalArgumentException("Items must be in a range between 1 and 15.");
        }

        this.maxWeight = maxWeight;
        this.items = Collections.unmodifiableList(items);
    }
}
