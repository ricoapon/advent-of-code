package nl.ricoapon.year2021.day16;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class MessageIntoPacketTranslator {
    private final MessageReader messageReader;
    private final List<Consumer<Integer>> readerConsumers = new ArrayList<>();

    public MessageIntoPacketTranslator(MessageReader messageReader) {
        this.messageReader = messageReader;
    }

    public Packet translate() {
        Packet packet = readPacket();

        // As a check we determine if all the other strings are zero.
        String leftover = messageReader.readTheRest();
        boolean otherChars = leftover.chars().anyMatch(c -> c != '0');
        if (otherChars) {
            throw new RuntimeException("We are left with a non-zero leftover string: '" + leftover + "'");
        }

        return packet;
    }

    private Packet readPacket() {
        int version = readVersion();
        int typeId = readTypeId();
        Packet.Type type = Packet.Type.of(typeId);

        if (Packet.Type.VALUE == type) {
            long literalValue = readLiteralValue();
            return new Packet.LiteralValuePacket(version, typeId, type, literalValue);
        } else {
            List<Packet> subPackets = readSubPackets();
            return new Packet.OperatorPacket(version, typeId, type, subPackets);
        }
    }

    private int readVersion() {
        return Integer.parseInt(readNBits(3), 2);
    }

    private int readTypeId() {
        return Integer.parseInt(readNBits(3), 2);
    }

    private long readLiteralValue() {
        StringBuilder literalValueBinary = new StringBuilder();

        while (true) {
            String next = readNBits(5);
            if (next.startsWith("0")) {
                literalValueBinary.append(next.substring(1));
                break;
            }

            literalValueBinary.append(next.substring(1));
        }

        return Long.parseLong(literalValueBinary.toString(), 2);
    }

    private List<Packet> readSubPackets() {
        int lengthTypeId = Integer.parseInt(readNBits(1));

        if (lengthTypeId == 0) {
            return readSubPacketsTotalLength();
        } else {
            return readNumberOfSubPackets();
        }
    }

    private List<Packet> readSubPacketsTotalLength() {
        int totalLength = Integer.parseInt(readNBits(15), 2);

        List<Packet> subPackets = new ArrayList<>();

        AtomicInteger nrOfBitsRead = new AtomicInteger();
        Consumer<Integer> consumer = nrOfBitsRead::addAndGet;
        readerConsumers.add(consumer);

        while (nrOfBitsRead.get() < totalLength) {
            subPackets.add(readPacket());
        }

        readerConsumers.remove(consumer);

        return subPackets;
    }

    private List<Packet> readNumberOfSubPackets() {
        int nrOfSubPackets = Integer.parseInt(readNBits(11), 2);

        List<Packet> subPackets = new ArrayList<>();
        for (int i = 0; i < nrOfSubPackets; i++) {
            subPackets.add(readPacket());
        }

        return subPackets;
    }

    private String readNBits(int n) {
        readerConsumers.forEach(consumer -> consumer.accept(n));
        return messageReader.readNBits(n);
    }
}
