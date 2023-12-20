package nl.ricoapon.year2021.day16;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay16 implements Algorithm {
    @Override
    public Object part1(String input) {
        MessageReader messageReader = new MessageReader(input);
        MessageIntoPacketTranslator messageIntoPacketTranslator = new MessageIntoPacketTranslator(messageReader);
        Packet packet = messageIntoPacketTranslator.translate();
        return String.valueOf(packet.calculateVersion());
    }

    @Override
    public Object part2(String input) {
        MessageReader messageReader = new MessageReader(input);
        MessageIntoPacketTranslator messageIntoPacketTranslator = new MessageIntoPacketTranslator(messageReader);
        Packet packet = messageIntoPacketTranslator.translate();
        return String.valueOf(packet.calculateValue());
    }
}
