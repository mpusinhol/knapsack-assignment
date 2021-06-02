package com.mobiquity.implementation.packer;

import com.mobiquity.implementation.dto.Item;
import com.mobiquity.implementation.dto.TestCase;
import com.mobiquity.implementation.exception.APIException;
import com.mobiquity.implementation.utils.PackerFileParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Packer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Packer.class);

    private Packer() {}

    public static String pack(String filePath) throws APIException {
        List<TestCase> testCases = null;

        try {
            testCases = PackerFileParser.parseFile(filePath);
        } catch (Exception e) {
            throw new APIException("Error while reading file arguments. " + e.getMessage(), e);
        }

        String solution = testCases.stream()
                .map(testCase -> getMaximizedPackage(testCase.getMaxWeight(), testCase.getItems(), 0, new HashMap<>()))
                .map(items -> {
                    String caseResult = items.stream()
                            .map(item -> String.valueOf(item.getId()))
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
}
