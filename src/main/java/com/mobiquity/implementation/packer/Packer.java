package com.mobiquity.implementation.packer;

import com.mobiquity.implementation.dto.Item;
import com.mobiquity.implementation.dto.TestCase;
import com.mobiquity.implementation.exception.APIException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Packer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Packer.class);

    private static final String TEST_CASE_SEPARATOR = "\n";
    private static final String TEST_CASE_CONTENT_SEPARATOR = " ";
    private static final String ITEM_FIELDS_SEPARATOR = ",";

    private Packer() {}

    public static String pack(String filePath) throws APIException {
        List<TestCase> testCases = null;

        try {
            testCases = parseFile(filePath);
        } catch (Exception e) {
            throw new APIException("Error while reading file arguments. " + e.getMessage(), e);
        }

        String solution = testCases.stream()
                .map(testCase -> getMaximizedPackage(testCase.getMaxWeight(), testCase.getItems(), 0, new HashMap<>()))
                .map(items -> {
                    String caseResult = items.stream()
                            .map(item -> item.getId().toString())
                            .reduce("", (result, itemId) -> result += (itemId + ","));

                    return "".equals(caseResult) ?
                            "-" : StringUtils.reverse(StringUtils.chop(caseResult));
                })
                .reduce("", (results, result) -> results += (result + "\n"));

        return StringUtils.chop(solution);
    }

    private static List<Item> getMaximizedPackage(double maxWeight, List<Item> items, int itemIndex,
                                                  Map<String, List<Item>> cache) {
        String key = maxWeight + ";" + itemIndex;

        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        if (maxWeight <= 0 || itemIndex == items.size()) {
            return new ArrayList<>();
        }

        List<Item> result = null;
        Item item = items.get(itemIndex);

        if (item.getWeight() > maxWeight) {
            result = getMaximizedPackage(maxWeight, items, ++itemIndex, cache);
        } else {
            itemIndex++;
            List<Item> inclusiveList = getMaximizedPackage(maxWeight - item.getWeight(), items, itemIndex, cache);
            List<Item> exclusiveList = getMaximizedPackage(maxWeight, items, itemIndex, cache);

            inclusiveList.add(item);

            result = getMostValuableList(inclusiveList, exclusiveList);
        }

        cache.put(key, result);

        return result;
    }

    private static List<Item> getMostValuableList(List<Item> inclusiveList, List<Item> exclusiveList) {
        double exclusiveValueSum = 0.0;
        double exclusiveWeightSum = 0.0;
        double inclusiveValueSum = 0.0;
        double inclusiveWeightSum = 0.0;

        for (Item obj : exclusiveList) {
            exclusiveValueSum += obj.getValue();
            exclusiveWeightSum += obj.getWeight();
        }

        for (Item obj : inclusiveList) {
            inclusiveValueSum += obj.getValue();
            inclusiveWeightSum += obj.getWeight();
        }

        if (inclusiveValueSum > exclusiveValueSum) {
            return inclusiveList;
        } else if (exclusiveValueSum > inclusiveValueSum) {
            return exclusiveList;
        } else {
            return inclusiveWeightSum < exclusiveWeightSum ? inclusiveList : exclusiveList;
        }
    }

    private static List<TestCase> parseFile(String filePath) throws IOException {
        final String fileContent = Files.readString(Path.of(filePath));

        final String[] lines = fileContent.split(TEST_CASE_SEPARATOR);

        return Arrays.stream(lines)
                .map(Packer::parseLine)
                .collect(Collectors.toList());
    }

    private static TestCase parseLine(String line) {
        final String[] content = line.split(TEST_CASE_CONTENT_SEPARATOR);

        final double maxWeight = Double.valueOf(content[0]);

        final List<Item> items = IntStream.range(2, content.length)
                .mapToObj(i -> content[i])
                .map(Packer::parseItem)
                .collect(Collectors.toList());

        return new TestCase(maxWeight, items);
    }

    private static Item parseItem(String itemStr) {
        final String itemWithoutParenthesis = itemStr.substring(1, itemStr.length() - 1);

        final String[] fields = itemWithoutParenthesis.split(ITEM_FIELDS_SEPARATOR);

        final Integer index = Integer.valueOf(fields[0]);
        final Double weight = Double.valueOf(fields[1]);
        final Double value = Double.valueOf(fields[2].substring(1));

        return new Item(index, weight, value);
    }
}
