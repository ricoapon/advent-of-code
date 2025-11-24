package nl.ricoapon.year2024.day9;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay9 implements Algorithm {

    class DiskMapIterator implements Iterator<Integer> {
        private final List<Integer> diskMap;
        private int indexFirst = 0;
        private int indexSecond = 0;
        private int reverseIndexFirst;
        private int reverseIndexSecond;

        public DiskMapIterator(String diskMapString) {
            this.diskMap = Stream.of(diskMapString.split("")).map(Integer::parseInt).toList();
            // We need to start with an index that refers to a non-free space.
            reverseIndexFirst = diskMap.size() - 1;
            if (reverseIndexFirst % 2 == 1) {
                reverseIndexFirst--;
            }
            
            // We want 0 to be the last (correct) index, so we do minus one.
            reverseIndexSecond = diskMap.get(reverseIndexFirst) - 1;
        }

        @Override
        public boolean hasNext() {
            // We can still continue if the first set of indices is not past the second set
            // of indices.
            if (indexFirst > reverseIndexFirst) {
                return false;
            } else if (indexFirst < reverseIndexFirst) {
                return true;
            }
            // The first index is equal. So it relies on the second index only.
            return indexSecond <= reverseIndexSecond;
        }

        @Override
        public Integer next() {
            int id = determineId();
            incrementIndices();
            return id;
        }

        private boolean isFreeSpace() {
            return indexFirst % 2 == 1;
        }

        private int determineId() {
            if (isFreeSpace()) {
                return reverseIndexFirst / 2;
            } else {
                return indexFirst / 2;
            }
        }

        private void incrementIndices() {
            if (isFreeSpace()) {
                // We also have to decrease reverse index, since we selected one.
                reverseIndexSecond--;
                if (reverseIndexSecond < 0) {
                    // When we make a step, we have to make two steps!
                    // We need to skip the free space.
                    reverseIndexFirst -= 2;
                    // We want 0 to be the last (correct) index, so we do minus one.
                    reverseIndexSecond = diskMap.get(reverseIndexFirst) - 1;
                }
                // It could be that the next step lands on a value that is 0.
                // We skip those until we land on a value.
                while (diskMap.get(reverseIndexFirst) == 0) {
                    reverseIndexFirst -= 2;
                    reverseIndexSecond = diskMap.get(reverseIndexFirst) - 1;
                }
            }

            indexSecond++;
            if (diskMap.get(indexFirst) <= indexSecond) {
                indexFirst++;
                indexSecond = 0;
            }
            // It could be that the next step lands on a value that is 0.
            // We skip those until we land on a value.
            while (diskMap.get(indexFirst) == 0) {
                indexFirst++;
            }
        }

    }

    @Override
    public Object part1(String input) {
        DiskMapIterator iterator = new DiskMapIterator(input);
        long checksum = 0;
        int i = 0;
        while (iterator.hasNext()) {
            var next = iterator.next();
            checksum += i * next;
            i++;
        }

        return checksum;
    }

    @Override
    public Object part2(String input) {
        return "x";
    }
}
