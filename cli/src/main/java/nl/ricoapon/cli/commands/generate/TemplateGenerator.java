package nl.ricoapon.cli.commands.generate;

public class TemplateGenerator {
    public String generateAlgorithmDay(int day) {
        return """
                package nl.ricoapon.day%s;
                
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
                """.formatted(day, day);
    }

    public String generateAlgorithmDayTest(int day) {
        return """
                package nl.ricoapon.day%s;
                
                import nl.ricoapon.framework.testrunner.AlgorithmDayTestRunnerUtil;
                import org.junit.jupiter.api.Test;
                
                class AlgorithmDay%sTest {
                    private final AlgorithmDayTestRunnerUtil algorithmDayTestRunnerUtil = new AlgorithmDayTestRunnerUtil(%s);
                
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
                """.formatted(day, day, day);
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
}
