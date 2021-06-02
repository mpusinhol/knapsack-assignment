package com.mobiquity.implementation.utils;

import com.mobiquity.implementation.dto.Item;
import com.mobiquity.implementation.dto.TestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PackerFileParser {

    private static final String TEST_CASE_SEPARATOR = "\n";
    private static final String TEST_CASE_CONTENT_SEPARATOR = " ";
    private static final String ITEM_FIELDS_SEPARATOR = ",";

    private PackerFileParser() {}

    public static List<TestCase> parseFile(String filePath) throws IOException {
        final String fileContent = Files.readString(Path.of(filePath));

        final String[] lines = fileContent.split(TEST_CASE_SEPARATOR);

        return Arrays.stream(lines)
                .map(PackerFileParser::parseLine)
                .collect(Collectors.toList());
    }

    public static TestCase parseLine(String line) {
        final String[] content = line.split(TEST_CASE_CONTENT_SEPARATOR);

        final double maxWeight = Double.valueOf(content[0]);

        final List<Item> items = IntStream.range(2, content.length)
                .mapToObj(i -> content[i])
                .map(PackerFileParser::parseItem)
                .collect(Collectors.toList());

        return new TestCase(maxWeight, items);
    }

    public static Item parseItem(String itemStr) {
        final String itemWithoutParenthesis = itemStr.substring(1, itemStr.length() - 1);

        final String[] fields = itemWithoutParenthesis.split(ITEM_FIELDS_SEPARATOR);

        final Integer index = Integer.valueOf(fields[0]);
        final Double weight = Double.valueOf(fields[1]);
        final Double value = Double.valueOf(fields[2].substring(1));

        return new Item(index, weight, value);
    }
}
