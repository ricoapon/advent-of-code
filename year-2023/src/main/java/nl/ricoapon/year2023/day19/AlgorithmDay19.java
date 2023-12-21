package nl.ricoapon.year2023.day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay19 implements Algorithm {
    record Part(long x, long m, long a, long s) {

        private final static Pattern PART = Pattern.compile("\\{x=(\\d+),m=(\\d+),a=(\\d+),s=(\\d+)}");
        public static Part of(String line) {
            Matcher m = PART.matcher(line);
            if (!m.matches()) {
                throw new RuntimeException("This should never happen");
            }

            return new Part(
                    Long.valueOf(m.group(1)),
                    Long.valueOf(m.group(2)),
                    Long.valueOf(m.group(3)),
                    Long.valueOf(m.group(4)));
        }
    }

    enum RuleType {
        LARGER_THAN,
        SMALLER_OR_EQUAL_TO,
        ALWAYS_TRUE;
    }

    enum PartField {
        X(Part::x),
        M(Part::m),
        A(Part::a),
        S(Part::s);

        private final Function<Part, Long> get;

        private PartField(Function<Part, Long> get) {
            this.get = get;
        }

        public long getField(Part p) {
            return get.apply(p);
        }
    }

    record Rule(PartField partField, RuleType ruleType, long value, String nextWorkflow) {

        private final static Pattern CONDITION = Pattern.compile("(a|m|s|x)(>|<)(\\d+)");

        public static Rule of(String ruleAsString) {
            if (!ruleAsString.contains(":")) {
                // We have an rule that is always accepted, and the rule is just the name of the
                // next workflow.
                return new Rule(null, RuleType.ALWAYS_TRUE, 0, ruleAsString);
            }

            String nextWorkflow = ruleAsString.split(":")[1];
            String conditionAsString = ruleAsString.split(":")[0];
            Matcher conditionMatcher = CONDITION.matcher(conditionAsString);
            if (!conditionMatcher.matches()) {
                throw new RuntimeException("This should not happen");
            }

            long value = Long.valueOf(conditionMatcher.group(3));
            // How do you do this better??
            RuleType ruleType = switch (conditionMatcher.group(2)) {
                case ">" -> RuleType.LARGER_THAN;
                case "<" -> RuleType.SMALLER_OR_EQUAL_TO;
                default -> throw new RuntimeException();
            };
            // x < 5 is the same as x <= 4.
            if (ruleType.equals(RuleType.SMALLER_OR_EQUAL_TO)) {
                value--;
            }
            PartField partField = switch (conditionMatcher.group(1)) {
                case "x" -> PartField.X;
                case "m" -> PartField.M;
                case "a" -> PartField.A;
                case "s" -> PartField.S;
                default -> throw new RuntimeException();
            };

            return new Rule(partField, ruleType, value, nextWorkflow);
        }

        public Optional<String> apply(Part part) {
            if (RuleType.LARGER_THAN.equals(ruleType) && !(partField.getField(part) > value)) {
                return Optional.empty();
            } else if (RuleType.SMALLER_OR_EQUAL_TO.equals(ruleType) && !(partField.getField(part) <= value)) {
                return Optional.empty();
            }

            return Optional.of(nextWorkflow);
        }

        public Rule reverse() {
            if (RuleType.LARGER_THAN.equals(ruleType)) {
                return new Rule(partField, RuleType.SMALLER_OR_EQUAL_TO, value, null);
            } else if (RuleType.SMALLER_OR_EQUAL_TO.equals(ruleType)) {
                return new Rule(partField, RuleType.LARGER_THAN, value, null);
            }

            throw new RuntimeException("Cannot calculate reverse of always true rule");
        }
    }

    record Workflow(String identifier, List<Rule> rules) {
        public static Workflow of(String line) {
            String identifier = line.substring(0, line.indexOf('{'));
            List<Rule> rules = Arrays.stream(line.substring(line.indexOf('{') + 1, line.length() - 1).split(","))
                    .map(Rule::of).toList();
            return new Workflow(identifier, rules);
        }

        public String apply(Part part) {
            for (Rule rule : rules) {
                Optional<String> result = rule.apply(part);
                if (result.isEmpty()) {
                    continue;
                }
                return result.get();
            }

            throw new RuntimeException("No rules matched part in workflow " + identifier
                    + ", but there should always be a final rule that matches");
        }
    }

    public static Map<String, Workflow> ofWorkflows(String input) {
        String workflowInput = input.split("\\r?\\n\\r?\\n")[0];
        return Arrays.stream(workflowInput.split("\\r?\\n")).map(Workflow::of)
                .collect(Collectors.toMap(Workflow::identifier, w -> w));
    }

    public static List<Part> ofParts(String input) {
        String partsInput = input.split("\\r?\\n\\r?\\n")[1];
        return Arrays.stream(partsInput.split("\\r?\\n")).map(Part::of).toList();
    }

    @Override
    public Object part1(String input) {
        Map<String, Workflow> workflows = ofWorkflows(input);
        List<Part> parts = ofParts(input);

        return calculateAccepted(workflows, parts).stream().mapToLong(p -> p.x + p.m + p.a + p.s).sum();
    }

    public List<Part> calculateAccepted(Map<String, Workflow> workflows, List<Part> parts) {
        return parts.stream().filter(p -> {
            String currentWorkflow = "in";
            while (!"R".equals(currentWorkflow) && !"A".equals(currentWorkflow)) {
                currentWorkflow = workflows.get(currentWorkflow).apply(p);
            }
            return "A".equals(currentWorkflow);
        }).toList();
    }

    @Override
    public Object part2(String input) {
        Map<String, Workflow> workflows = ofWorkflows(input);

        return calculateCombinationsRecursively(workflows, new ArrayList<>(), workflows.get("in"), new HashSet<>());
    }

    private long calculateCombinationsRecursively(Map<String, Workflow> workflows, List<Rule> followedRules,
            Workflow currentWorkflow, Set<List<Rule>> discovered) {
        long combinations = 0;
        for (Rule rule : currentWorkflow.rules) {
            followedRules.add(rule);

            if ("A".equals(rule.nextWorkflow)) {
                if (!discovered.contains(followedRules)) {
                    combinations += determinePossibleCombinations(followedRules);
                    discovered.add(new ArrayList<>(followedRules.stream().toList()));
                }
            } else if ("R".equals(rule.nextWorkflow)) {
                // We do not want to count these! We stop here.
            } else {
                Workflow nextWorkflow = workflows.get(rule.nextWorkflow);
                combinations += calculateCombinationsRecursively(workflows, followedRules, nextWorkflow, discovered);
            }

            followedRules.remove(rule);
            // We are not following the rule anymore, which means we must follow the reverse
            // of the rule!
            if (!RuleType.ALWAYS_TRUE.equals(rule.ruleType)) {
                followedRules.add(rule.reverse());
            }
        }

        // We added reverse rules, but we need to undo those here.
        currentWorkflow.rules.stream().filter(r -> !RuleType.ALWAYS_TRUE.equals(r.ruleType)).forEach(r -> followedRules.remove(r.reverse()));

        return combinations;
    }

    private final static List<Rule> mandatoryRules = List.of(
            new Rule(PartField.X, RuleType.LARGER_THAN, 0, null),
            new Rule(PartField.X, RuleType.SMALLER_OR_EQUAL_TO, 4000, null),
            new Rule(PartField.M, RuleType.LARGER_THAN, 0, null),
            new Rule(PartField.M, RuleType.SMALLER_OR_EQUAL_TO, 4000, null),
            new Rule(PartField.A, RuleType.LARGER_THAN, 0, null),
            new Rule(PartField.A, RuleType.SMALLER_OR_EQUAL_TO, 4000, null),
            new Rule(PartField.S, RuleType.LARGER_THAN, 0, null),
            new Rule(PartField.S, RuleType.SMALLER_OR_EQUAL_TO, 4000, null));

    long determinePossibleCombinations(List<Rule> appliedRules) {
        // By always adding these mandatory rules, we make sure that:
        // 1. We satisfy the boundaries given by the exercise
        // 2. We don't have to worry about not finding upper/lower bounds
        List<Rule> rulesToUse = Stream.concat(mandatoryRules.stream(), appliedRules.stream()).toList();

        // We multiply the combinations, so we need to start with 1, not that there is
        // always a possible combination!
        long nrOfCombinations = 1;
        for (PartField partField : PartField.values()) {
            // All the "x > 5" rules give us a lower bound on x, so we can pick the largest
            // value and that is our lower buond.
            long lowerBoundInclusive = rulesToUse.stream()
                    .filter(r -> r.ruleType.equals(RuleType.LARGER_THAN))
                    .filter(r -> r.partField.equals(partField))
                    .mapToLong(Rule::value)
                    .max().orElseThrow();
            // All the "x <= 5" rules give us an upper bound on x, so we can pick the
            // smallest value and that is our upper bound.
            long upperBoundInclusive = rulesToUse.stream()
                    .filter(r -> r.ruleType.equals(RuleType.SMALLER_OR_EQUAL_TO))
                    .filter(r -> r.partField.equals(partField))
                    .mapToLong(Rule::value)
                    .min().orElseThrow();

            if (lowerBoundInclusive == upperBoundInclusive) {
                // Only a single combination exists.
                continue;
            } else if (lowerBoundInclusive > upperBoundInclusive) {
                // We found an impossible combination.
                return 0;
            } else {
                nrOfCombinations = nrOfCombinations * (upperBoundInclusive - lowerBoundInclusive);
            }
        }

        return nrOfCombinations;
    }
}
