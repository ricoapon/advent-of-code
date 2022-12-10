package nl.ricoapon.day10;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay10 implements Algorithm {
    @Override
    public String part1(String input) {
        CommunicationDevice communicationDevice = new CommunicationDevice();

        for (String line : input.split("\r?\n")) {
            if ("noop".equals(line)) {
                communicationDevice.noop();
            } else {
                communicationDevice.add(Integer.parseInt(line.substring(5)));
            }
        }

        return "" + communicationDevice.getCycleValuesSum();
    }

    @Override
    public String part2(String input) {
        CRTDrawingDevice crtDrawingDevice = new CRTDrawingDevice();

        for (String line : input.split("\r?\n")) {
            if ("noop".equals(line)) {
                crtDrawingDevice.noop();
            } else {
                crtDrawingDevice.add(Integer.parseInt(line.substring(5)));
            }
        }

        return crtDrawingDevice.getDrawing();
    }
}
