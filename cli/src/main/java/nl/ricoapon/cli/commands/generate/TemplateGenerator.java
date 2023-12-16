package nl.ricoapon.cli.commands.generate;

public class TemplateGenerator {
    public String generateAlgorithmDay(int year, int day) {
        return """
                package nl.ricoapon.year%s.day%s;
                
                import nl.ricoapon.framework.Algorithm;
                
                public class AlgorithmDay%s implements Algorithm {
                    @Override
                    public String part1(String input) {
                        return "x";
                    }
                
                    @Override
                    public String part2(String input) {
                        return "x";
                    }
                }
                """.formatted(year, day, day);
    }

    public String generateAlgorithmDayTest(int year, int day) {
        return """
                package nl.ricoapon.year%s.day%s;
                
                import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
                import org.junit.jupiter.api.Test;
                
                class AlgorithmDay%sTest {
                    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(%s, %s);
                    
                    @Test
                    void part1_example() {
                        algorithmDayTestRunnerUtil.testAllExamples(1);
                    }
                    
                    @Test
                    void part1() {
                        algorithmDayTestRunnerUtil.testInput(1);
                    }
                    
                    @Test
                    void part2_example() {
                        algorithmDayTestRunnerUtil.testAllExamples(2);
                    }
                    
                    @Test
                    void part2() {
                        algorithmDayTestRunnerUtil.testInput(2);
                    }
                }
                """.formatted(year, day, day, year, day);
    }

    public String generateExpectedYaml() {
        return """
                part1:
                  input: x
                  example: x
                part2:
                  input: x
                  example: x
                """;
    }

    public String generatePom(int year) {
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <project xmlns="http://maven.apache.org/POM/4.0.0"
                         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                    <modelVersion>4.0.0</modelVersion>
                    <parent>
                        <groupId>nl.ricoapon.advent-of-code</groupId>
                        <artifactId>year-parent</artifactId>
                        <version>1.0-SNAPSHOT</version>
                        <relativePath>../year-parent/pom.xml</relativePath>
                    </parent>
                                
                    <artifactId>year-%s</artifactId>
                </project>
                """.formatted(year);
    }
}
