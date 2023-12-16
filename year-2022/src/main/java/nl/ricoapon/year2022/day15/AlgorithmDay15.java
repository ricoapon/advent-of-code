package nl.ricoapon.year2022.day15;

import nl.ricoapon.Coordinate2D;
import nl.ricoapon.framework.Algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.abs;

public class AlgorithmDay15 implements Algorithm {
    record SensorBeacon(Coordinate2D sensor, Coordinate2D beacon) {
        public int distance() {
            return AlgorithmDay15.distance(sensor, beacon);
        }
    }

    private static int distance(Coordinate2D a, Coordinate2D b) {
        return abs(a.x() - b.x()) + abs(a.y() - b.y());
    }

    private Set<Coordinate2D> circleAroundCIntersectionWithY(Coordinate2D m, int radius, int y) {
        // We can calculate the intersection directly. If there are possible values, it should be these.
        // We only have to check if they are valid by checking the radius.
        int verticalDistance = abs(m.y() - y);
        if (verticalDistance > radius) {
            return Set.of();
        } else {
            verticalDistance = radius;
        }
        return IntStream.range(-verticalDistance, verticalDistance + 1).mapToObj(i -> new Coordinate2D(m.x() + i, y))
                .filter(s -> distance(m, s) <= radius)
                .collect(Collectors.toSet());
    }


    private final static Pattern LINE = Pattern.compile("Sensor at x=(.*), y=(.*): closest beacon is at x=(.*), y=(.*)");

    private SensorBeacon determineSensorBeacon(String line) {
        Matcher matcher = LINE.matcher(line);
        if (!matcher.matches()) {
            throw new RuntimeException("This should not happen");
        }
        return new SensorBeacon(
                new Coordinate2D(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))),
                new Coordinate2D(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)))
        );
    }

    @Override
    public String part1(String input) {
        boolean isExample = input.startsWith("Sensor at x=2,");
        int y;
        if (isExample) {
            y = 10;
        } else {
            y = 2_000_000;
        }

        List<SensorBeacon> sensorBeaconList = Arrays.stream(input.split("\r?\n"))
                .map(this::determineSensorBeacon)
                .toList();

        Set<Coordinate2D> beacons = sensorBeaconList.stream().map(SensorBeacon::beacon).collect(Collectors.toSet());
        Set<Coordinate2D> sensors = sensorBeaconList.stream().map(SensorBeacon::sensor).collect(Collectors.toSet());
        var notBeaconSensor = sensorBeaconList.stream()
                .flatMap(sb -> circleAroundCIntersectionWithY(sb.sensor(), sb.distance(), y).stream())
                .filter(c -> !beacons.contains(c))
                .filter(c -> !sensors.contains(c))
                .collect(Collectors.toSet());

        return "" + notBeaconSensor.size();
    }

    @Override
    public String part2(String input) {
        return "x";
    }
}
