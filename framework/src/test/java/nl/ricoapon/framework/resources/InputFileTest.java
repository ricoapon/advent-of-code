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
            new InputFile(path);
        }

        @ParameterizedTest
        @ValueSource(strings = {"/day26/input.txt", "/day5/part1_example1", "day17/part2_example5.txt", "/day1/part2_example.txt"})
        void invalidPathsThrowAnException(String path) {
            assertThrows(IllegalArgumentException.class, () -> new InputFile(path));
        }
    }

    @Test
    void isExampleWorks() {
        assertTrue(new InputFile("/day1/part1_example2.txt").isExample());
        assertFalse(new InputFile("/day1/input.txt").isExample());
    }

    @Test
    void dayWorks() {
        assertEquals(1, new InputFile("/day1/part1_example2.txt").day());
        assertEquals(17, new InputFile("/day17/part2_example1.txt").day());
        assertEquals(25, new InputFile("/day25/input.txt").day());
    }

    @Test
    void partWorks() {
        assertEquals(1, new InputFile("/day1/part1_example2.txt").part());
        assertEquals(2, new InputFile("/day17/part2_example1.txt").part());
    }

    @Test
    void exampleNumberWorks() {
        assertEquals(3, new InputFile("/day1/part1_example3.txt").exampleNumber());
        assertEquals(1, new InputFile("/day17/part2_example1.txt").exampleNumber());
    }

    @Test
    void existsAndReadContentWork() {
        InputFile day1Input = new InputFile("/day1/input.txt");
        assertTrue(day1Input.exists());
        assertEquals("input", day1Input.readContent());

        InputFile nonExistingInput = new InputFile("/day3/input.txt");
        assertFalse(nonExistingInput.exists());
        assertThrows(RuntimeException.class, nonExistingInput::readContent);
    }
}
