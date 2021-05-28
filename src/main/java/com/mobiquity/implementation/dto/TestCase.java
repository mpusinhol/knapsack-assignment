package com.mobiquity.implementation.dto;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class TestCase {

    private static final double MAX_WEIGHT_ALLOWED = 100;
    private static final int MAX_ITEMS_ALLOWED = 15;

    private final double maxWeight;
    private final List<Item> items;

    public TestCase(double maxWeight, List<Item> items) {
        if (maxWeight < 0 || maxWeight > MAX_WEIGHT_ALLOWED) {
            throw new IllegalArgumentException("Max weight must be in between a range of 0 and 100.");
        }

        if (items == null || items.isEmpty() || items.size() > MAX_ITEMS_ALLOWED) {
            throw new IllegalArgumentException("Items must be in a range between 1 and 15.");
        }

        this.maxWeight = maxWeight;
        this.items = Collections.unmodifiableList(items);
    }
}
