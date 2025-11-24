package nl.ricoapon.framework.resources;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.Map;

public class Expected {
    public Map<String, String> part1;
    public Map<String, String> part2;

    public static Expected of(String yamlAsString) {
        Yaml yaml = new Yaml(new Constructor(Expected.class, new LoaderOptions()));
        Expected expected = yaml.load(yamlAsString);

        if (expected.part1 == null || expected.part2 == null) {
            throw new RuntimeException("You have to specify at least the input into the expected.yml for each part");
        }

        return expected;
    }

    @Override
    public String toString() {
        return "Expected{" +
                "part1=" + part1 +
                ", part2=" + part2 +
                '}';
    }
}
