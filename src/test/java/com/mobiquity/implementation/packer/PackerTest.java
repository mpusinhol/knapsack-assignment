package com.mobiquity.implementation.packer;

import com.mobiquity.implementation.exception.APIException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PackerTest {

    private static final String HAPPY_FLOW_INPUT = "happy-flow-input.txt";
    private static final String HAPPY_FLOW_OUTPUT = "happy-flow-output.txt";
    private static final String NEGATIVE_PACKAGE_WEIGHT_INPUT = "negative-package-weight-input.txt";
    private static final String OVER_HUNDRED_PACKAGE_WEIGHT_INPUT = "over-hundred-package-weight-input.txt";
    private static final String ZERO_ITEMS_INPUT = "zero-items-input.txt";
    private static final String OVER_ITEMS_LIMIT_INPUT = "over-items-limit-input.txt";

    private static final String DEFAULT_EXCEPTION_MESSAGE = "Error while reading file arguments.";

    String getResourceAbsolutePath(String resource) {
        File file = new File(getClass().getResource(resource).getFile());
        return file.getAbsolutePath();
    }

    @Test
    void testHappyFlowPack() throws APIException, IOException {
        String input = getResourceAbsolutePath(HAPPY_FLOW_INPUT);
        Path expectedOutputPath = Path.of(getResourceAbsolutePath(HAPPY_FLOW_OUTPUT));
        String expectedOutput = Files.readString(expectedOutputPath);

        String output = Packer.pack(input);

        Assertions.assertEquals(expectedOutput, output);
    }

    @Test
    void testNegativePackageWeight() {
        String expectedMessage = DEFAULT_EXCEPTION_MESSAGE + " Max weight must be in between a range of 0 and 100.";
        String input = getResourceAbsolutePath(NEGATIVE_PACKAGE_WEIGHT_INPUT);

        APIException e = Assertions.assertThrows(APIException.class, () -> Packer.pack(input));

        Assertions.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    void testOverHundredPackageWeight() {
        String expectedMessage = DEFAULT_EXCEPTION_MESSAGE + " Max weight must be in between a range of 0 and 100.";
        String input = getResourceAbsolutePath(OVER_HUNDRED_PACKAGE_WEIGHT_INPUT);

        APIException e = Assertions.assertThrows(APIException.class, () -> Packer.pack(input));

        Assertions.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    void testZeroItems() {
        String expectedMessage = DEFAULT_EXCEPTION_MESSAGE + " Items must be in a range between 1 and 15.";
        String input = getResourceAbsolutePath(ZERO_ITEMS_INPUT);

        APIException e = Assertions.assertThrows(APIException.class, () -> Packer.pack(input));

        Assertions.assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    void testOverItemsLimit() {
        String expectedMessage = DEFAULT_EXCEPTION_MESSAGE + " Items must be in a range between 1 and 15.";
        String input = getResourceAbsolutePath(OVER_ITEMS_LIMIT_INPUT);

        APIException e = Assertions.assertThrows(APIException.class, () -> Packer.pack(input));

        Assertions.assertEquals(expectedMessage, e.getMessage());
    }
}
