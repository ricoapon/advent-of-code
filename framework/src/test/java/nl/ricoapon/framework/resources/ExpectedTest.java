package nl.ricoapon.framework.resources;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpectedTest {

    @Test
    void partsAreReadIntoMaps() {
        // Given
        String yaml = """
                part1:
                  input.txt: 1
                  example.txt: 2
                part2:
                  input: 3
                  example: 4""";

        // When
        Expected expected = Expected.of(yaml);

        // Then
        assertThat(expected.part1, hasEntry("input.txt", "1"));
        assertThat(expected.part1, hasEntry("example.txt", "2"));
        assertThat(expected.part2, hasEntry("input", "3"));
        assertThat(expected.part2, hasEntry("example", "4"));
    }

    @Test
    void throwExceptionWhenAnyPartIsMissing() {
        // Given
        String yaml1 = """
                part1:
                  input.txt: 1
                  example.txt: 2""";
        String yaml2 = """
                part1:
                  input.txt: 1
                  example.txt: 2
                part2:""";

        assertThrows(RuntimeException.class, () -> Expected.of(yaml1));
        assertThrows(RuntimeException.class, () -> Expected.of(yaml2));

    }
}
