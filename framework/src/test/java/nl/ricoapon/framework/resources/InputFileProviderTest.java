package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputFileProviderTest {

    @Test
    void filesFromResourcesAreFoundAndOrderedCorrectly() {
        // This test basically covers everything. It is hard to
        List<InputFile> inputFiles = InputFileProvider.getAllInputFiles(1);

        assertEquals(4, inputFiles.size());

        assertFalse(inputFiles.get(0).isExample());

        assertEquals(1, inputFiles.get(1).part());
        assertEquals(1, inputFiles.get(1).exampleNumber());

        assertEquals(1, inputFiles.get(2).part());
        assertEquals(2, inputFiles.get(2).exampleNumber());

        assertEquals(2, inputFiles.get(3).part());
        assertEquals(1, inputFiles.get(3).exampleNumber());
    }

    @Test
    void exampleFilesForWhichThePreviousOneDoesNotExistIsIgnored() {
        assertEquals(1, InputFileProvider.getAllInputFiles(2).size());
    }
}
