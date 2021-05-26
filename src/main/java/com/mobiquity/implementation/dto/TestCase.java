package com.mobiquity.implementation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TestCase {

    private final double maxWeight;
    private final List<Item> items;
}
