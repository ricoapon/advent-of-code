package nl.ricoapon.day16;

import nl.ricoapon.framework.Algorithm;

public class AlgorithmDay16 implements Algorithm {
    @Override
    public String part1(String input) {
        MessageReader messageReader = new MessageReader(input);
        MessageIntoPacketTranslator messageIntoPacketTranslator = new MessageIntoPacketTranslator(messageReader);
        Packet packet = messageIntoPacketTranslator.translate();
        return String.valueOf(packet.calculateVersion());
    }

    @Override
    public String part2(String input) {
        MessageReader messageReader = new MessageReader(input);
        MessageIntoPacketTranslator messageIntoPacketTranslator = new MessageIntoPacketTranslator(messageReader);
        Packet packet = messageIntoPacketTranslator.translate();
        return String.valueOf(packet.calculateValue());
    }
}
