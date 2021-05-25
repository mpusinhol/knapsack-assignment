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
}
