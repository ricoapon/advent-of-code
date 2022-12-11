package nl.ricoapon.day11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MonkeyGame {
    private final List<Monkey> monkeys;
    // Use AtomicInteger type, so we can mutate the element instead of overriding the element in the list.
    private final List<AtomicInteger> monkeyInspectionCount;
    // We only need to know if the value is divisible by something. If we work modulo the product of all the divisibleBy numbers,
    // then the result will still be correct. And this prevents the high numbers.
    private final long maxValue;

    public MonkeyGame(List<Monkey> monkeys) {
        this.monkeys = monkeys;
        monkeyInspectionCount = new ArrayList<>();
        long maxValue = 1;
        for (int i = 0; i < monkeys.size(); i++) {
            monkeyInspectionCount.add(new AtomicInteger(0));
            if (monkeys.get(i).id != i) {
                throw new RuntimeException("This should never happen");
            }
            maxValue = maxValue * monkeys.get(i).testDivisibleBy;
        }
        this.maxValue = maxValue * 3;
    }

    public void playRound(boolean isPart1) {
        for (Monkey monkey : monkeys) {
            while (!monkey.items.isEmpty()) {
                Long item = monkey.items.poll();
                item = monkey.operation.apply(item);
                if (isPart1) {
                    // Integer division is floored automatically.
                    item = item / 3;
                } else {
                    item = item % maxValue;
                }

                if (item % monkey.testDivisibleBy == 0) {
                    monkeys.get(monkey.monkeyIdIfTestIsTrue).items.add(item);
                } else {
                    monkeys.get(monkey.monkeyIdIfTestIsFalse).items.add(item);
                }

                monkeyInspectionCount.get(monkey.id).incrementAndGet();
            }
        }
    }

    public long getMonkeyBusiness() {
        List<Long> sortedCount = monkeyInspectionCount.stream().map(AtomicInteger::get).map(Long::valueOf).sorted(Collections.reverseOrder()).toList();
        return sortedCount.get(0) * sortedCount.get(1);
    }
}
