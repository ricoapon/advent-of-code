package nl.ricoapon.year2025.day8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay8 implements Algorithm {
    private record Coordinate3D(long x, long y, long z) {
        public static Coordinate3D of(String line) {
            List<Long> integers = Stream.of(line.split(",")).map(Long::valueOf).toList();
            return new Coordinate3D(integers.get(0), integers.get(1), integers.get(2));
        }

        // It doesn't matter that we don't take the square root. It is about sorting.
        public double distanceSquared(Coordinate3D other) {
            return Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2) + Math.pow(z - other.z, 2);
        }
    }

    // Two connections with edges (a,b) or (b,a) should be equal.
    private record Connection(Set<Coordinate3D> coordinates, double distance) implements Comparable<Connection> {
        public Connection(Coordinate3D a, Coordinate3D b, double distance) {
            this(Set.of(a, b), distance);
        }

        @Override
        public int compareTo(Connection other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    @Override
    public Object part1(String input) {
        List<Coordinate3D> coordinates = Stream.of(input.split("\\r?\\n")).map(Coordinate3D::of).toList();
        int nrOfConnectionsToMake = (coordinates.size() <= 20) ? 10 : 1000;

        // We will keep track of the shortest connections (in reverse order for speed).
        List<Connection> shortestConnections = new ArrayList<>();

        Map<Coordinate3D, Map<Coordinate3D, Double>> distances = new HashMap<>();
        for (Coordinate3D a : coordinates) {
            distances.put(a, new HashMap<>());
        }
        for (Coordinate3D a : coordinates) {
            for (Coordinate3D b : coordinates) {
                if (a == b) {
                    continue;
                }

                if (!distances.get(a).containsKey(b)) {
                    var d = a.distanceSquared(b);
                    distances.get(a).put(b, d);
                    distances.get(b).put(a, d);

                    if (shortestConnections.isEmpty() || shortestConnections.size() < nrOfConnectionsToMake
                            || d < shortestConnections.get(0).distance) {
                        // The distance is shorter than the longest available distance.
                        // So we add it to the list.
                        Connection connection = new Connection(a, b, d);
                        if (shortestConnections.size() >= nrOfConnectionsToMake) {
                            shortestConnections.remove(0);
                        }
                        shortestConnections.add(connection);
                        shortestConnections.sort(Collections.reverseOrder());
                    }
                }
            }
        }

        // We can determine all circuits using shortestConnections.
        List<Set<Coordinate3D>> circuits = new ArrayList<>();
        for (Connection connection : shortestConnections) {
            var iterator = connection.coordinates.iterator();
            var a = iterator.next();
            var b = iterator.next();

            var setContainingA = circuits.stream().filter(s -> s.contains(a)).findAny();
            var setContainingB = circuits.stream().filter(s -> s.contains(b)).findAny();

            if (setContainingA.isPresent() && setContainingB.isPresent()) {
                if (setContainingA.get() == setContainingB.get()) {
                    // The connection was already made, so we can just skip this.
                    continue;
                }

                // We found two different sets, meaning we connected two circuits. We need to
                // merge them.
                circuits.remove(setContainingB.get());
                setContainingA.get().addAll(setContainingB.get());
                continue;
            }

            // If only one is present, add the connection to that one set.
            if (setContainingA.isPresent()) {
                setContainingA.get().add(b);
                continue;
            }
            if (setContainingB.isPresent()) {
                setContainingB.get().add(a);
                continue;
            }

            // Neither one was found in any circuit. So we add a new one.
            circuits.add(new HashSet<>(connection.coordinates));
        }

        // Find the three biggest sizes.
        List<Integer> biggestCircuits = circuits.stream().map(c -> c.size()).sorted(Comparator.reverseOrder()).limit(3)
                .toList();

        return biggestCircuits.get(0) * biggestCircuits.get(1) * biggestCircuits.get(2);
    }

    @Override
    public Object part2(String input) {
        List<Coordinate3D> coordinates = Stream.of(input.split("\\r?\\n")).map(Coordinate3D::of).toList();

        // We will keep track of the shortest connections. Use a sorted set so that we
        // really add the
        // edges in order of shortest distances to longest.
        SortedSet<Connection> shortestConnections = new TreeSet<>();

        Map<Coordinate3D, Map<Coordinate3D, Double>> distances = new HashMap<>();
        for (Coordinate3D a : coordinates) {
            distances.put(a, new HashMap<>());
        }
        for (Coordinate3D a : coordinates) {
            for (Coordinate3D b : coordinates) {
                if (a == b) {
                    continue;
                }

                if (!distances.get(a).containsKey(b)) {
                    var d = a.distanceSquared(b);
                    distances.get(a).put(b, d);
                    distances.get(b).put(a, d);

                    Connection connection = new Connection(a, b, d);
                    shortestConnections.add(connection);
                }
            }
        }

        // We can determine all circuits using shortestConnections.
        List<Set<Coordinate3D>> circuits = new ArrayList<>();
        Connection finalConnection = null;

        // Looping over the SortedSet will automatically loop from shortest distance to
        // highest!
        for (Connection connection : shortestConnections) {
            var iterator = connection.coordinates.iterator();
            var a = iterator.next();
            var b = iterator.next();

            var setContainingA = circuits.stream().filter(s -> s.contains(a)).findAny();
            var setContainingB = circuits.stream().filter(s -> s.contains(b)).findAny();

            if (setContainingA.isPresent() && setContainingB.isPresent()) {
                if (setContainingA.get() != setContainingB.get()) {
                    // We found two different sets, meaning we connected two circuits. We need to
                    // merge them.
                    circuits.remove(setContainingB.get());
                    setContainingA.get().addAll(setContainingB.get());
                }
                // Else, we do nothing.
            } else if (setContainingA.isPresent()) {
                setContainingA.get().add(b);
            } else if (setContainingB.isPresent()) {
                setContainingB.get().add(a);
            } else {
                // Neither one was found in any circuit. So we add a new one.
                circuits.add(new HashSet<>(connection.coordinates));
            }

            if (circuits.get(0).size() == coordinates.size()) {
                finalConnection = connection;
                break;
            }
        }

        return finalConnection.coordinates.stream().mapToLong(c -> c.x).reduce((a, b) -> a * b).orElseThrow();
    }
}
