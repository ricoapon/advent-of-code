package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InputFileProviderTest {

    @Test
    void filesFromResourcesAreFoundAndOrderedCorrectly() {
        // This test basically covers everything.
        List<InputFile> inputFiles = InputFileProvider.getAllInputFiles(1);

        assertEquals(5, inputFiles.size());

        assertFalse(inputFiles.get(0).isExample());
        assertTrue(inputFiles.get(1).isExample());
        assertTrue(inputFiles.get(2).isExample());
        assertTrue(inputFiles.get(3).isExample());
        assertTrue(inputFiles.get(4).isExample());

        assertTrue(inputFiles.get(1).canBeUsedForPart1());
        assertTrue(inputFiles.get(2).canBeUsedForPart1());
        assertTrue(inputFiles.get(3).canBeUsedForPart1());
        assertFalse(inputFiles.get(4).canBeUsedForPart1());

        assertTrue(inputFiles.get(1).canBeUsedForPart2());
        assertFalse(inputFiles.get(2).canBeUsedForPart2());
        assertFalse(inputFiles.get(3).canBeUsedForPart2());
        assertTrue(inputFiles.get(4).canBeUsedForPart2());
    }

    @Test
    void exampleFilesForWhichThePreviousOneDoesNotExistIsIgnored() {
        assertEquals(1, InputFileProvider.getAllInputFiles(2).size());
    }

    @Test
    void returnEmptyListIfNoInputIsFound() {
        assertEquals(0, InputFileProvider.getAllInputFiles(3).size());
    }

    @Test
    void additionalExampleFilesAreAddedIfExampleDoesNotExist() {
        assertEquals(4, InputFileProvider.getAllInputFiles(4).size());
    }
}
