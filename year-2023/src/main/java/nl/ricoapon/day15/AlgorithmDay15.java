package nl.ricoapon.day15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nl.ricoapon.Pair;
import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay15 implements Algorithm {
    @Override
    public String part1(String input) {
        int result = Arrays.stream(input.split(","))
            .mapToInt(this::hash)
            .sum();
        return String.valueOf(result);
    }

    private int hash(String s) {
        int value = 0;
        for (char c : s.toCharArray()) {
            value = ((value + c) * 17) % 256;
        }
        return value;
    }

    @Override
    public String part2(String input) {
        Map<Integer, List<Pair<String, Integer>>> hashMap = new HashMap<>();

        List<String> items = Arrays.stream(input.split(",")).toList();

        for (String item : items) {
            if (item.contains("-")) {
                item = item.substring(0, item.length() - 1);
                int hash = hash(item);
                List<Pair<String, Integer>> list = hashMap.get(hash);
                if (list == null) {
                    continue;
                }
                removeElement(list, item);
            } else {
                int focalLength = Integer.valueOf(item.split("=")[1]);
                item = item.split("=")[0];
                int hash = hash(item);

                if (!hashMap.containsKey(hash)) {
                    hashMap.put(hash, new ArrayList<>());
                }

                List<Pair<String, Integer>> list = hashMap.get(hash);
                Optional<Pair<String, Integer>> foundObject = findInList(list, item);
                Pair<String, Integer> newObject = new Pair<String,Integer>(item, focalLength);
                if (foundObject.isPresent()) {
                    list.set(list.indexOf(foundObject.get()), newObject);
                } else {
                    list.add(newObject);
                }
            }
        }

        long score = 0;
        for (int i = 0; i < 256; i++) {
            if (!hashMap.containsKey(i)) {
                continue;
            }

            List<Pair<String, Integer>> list = hashMap.get(i);
            for (int j = 0; j < list.size(); j++) {
                score += (i + 1) * (j + 1) * list.get(j).getR();
            }
        }

        return String.valueOf(score);
    }

    private Optional<Pair<String, Integer>> findInList(List<Pair<String, Integer>> list, String element) {
        return list.stream().filter(p -> p.getL().equals(element)).findFirst();
    }

    private void removeElement(List<Pair<String, Integer>> list, String element) {
        Optional<Pair<String, Integer>> foundObject = findInList(list, element);
        if (foundObject.isEmpty()) {
            return;
        }
        list.remove(foundObject.get());
    }
}
