package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class InputFileTest {

    @Nested
    class ConstructorValidation {
        @ParameterizedTest
        @ValueSource(strings = {"/day25/input.txt", "/day5/part1_example1.txt", "/day17/part2_example5.txt", "/day1/part2_example0.txt"})
        void validPathsDoNotThrowAnException(String path) {
            InputFile.of(path);
        }

        @ParameterizedTest
        @ValueSource(strings = {"/day26/input.txt", "/day5/part1_example1", "day17/part2_example5.txt", "/day1/part2_example.txt"})
        void invalidPathsThrowAnException(String path) {
            assertThrows(IllegalArgumentException.class, () -> InputFile.of(path));
        }
    }

    @Test
    void isExampleWorks() {
        assertTrue(InputFile.of("/day1/part1_example2.txt").isExample());
        assertFalse(InputFile.of("/day1/input.txt").isExample());
    }

    @Test
    void partWorks() {
        assertTrue(InputFile.of("/day1/part1_example2.txt").canBeUsedForPart1());
        assertFalse(InputFile.of("/day1/part1_example2.txt").canBeUsedForPart2());

        assertFalse(InputFile.of("/day17/part2_example1.txt").canBeUsedForPart1());
        assertTrue(InputFile.of("/day17/part2_example1.txt").canBeUsedForPart2());
    }

    @Test
    void existsAndReadContentWork() {
        InputFile day1Input = InputFile.of("/day1/input.txt");
        assertTrue(day1Input.exists());
        assertEquals("input", day1Input.readContent());

        InputFile nonExistingInput = InputFile.of("/day3/input.txt");
        assertFalse(nonExistingInput.exists());
        assertThrows(RuntimeException.class, nonExistingInput::readContent);
    }
}
