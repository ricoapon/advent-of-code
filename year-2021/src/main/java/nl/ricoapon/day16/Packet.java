package nl.ricoapon.day16;

import java.util.List;
import java.util.function.Function;

public abstract class Packet {
    enum Type {
        SUM(0, (packet -> ((Packet.OperatorPacket) packet).getSubPackets().stream().mapToLong(Packet::calculateValue).sum())),
        PRODUCT(1, (packet -> ((Packet.OperatorPacket) packet).getSubPackets().stream().mapToLong(Packet::calculateValue).reduce(1L, (l, r) -> l * r))),
        MIN(2, (packet -> ((Packet.OperatorPacket) packet).getSubPackets().stream().mapToLong(Packet::calculateValue).min().orElseThrow())),
        MAX(3, (packet -> ((Packet.OperatorPacket) packet).getSubPackets().stream().mapToLong(Packet::calculateValue).max().orElseThrow())),
        GREATER_THAN(5, (packet -> {
            List<Packet> subPackets = ((Packet.OperatorPacket) packet).getSubPackets();
            if (subPackets.get(0).calculateValue() > subPackets.get(1).calculateValue()) {
                return 1L;
            }
            return 0L;
        })),
        LESS_THAN(6, (packet -> {
            List<Packet> subPackets = ((Packet.OperatorPacket) packet).getSubPackets();
            if (subPackets.get(0).calculateValue() < subPackets.get(1).calculateValue()) {
                return 1L;
            }
            return 0L;
        })),
        EQUAL(7, (packet -> {
            List<Packet> subPackets = ((Packet.OperatorPacket) packet).getSubPackets();
            if (subPackets.get(0).calculateValue() == subPackets.get(1).calculateValue()) {
                return 1L;
            }
            return 0L;
        })),

        VALUE(4, (packet -> ((Packet.LiteralValuePacket) packet).getLiteralValue()));

        private final int id;
        private final Function<Packet, Long> determineValue;

        Type(int id, Function<Packet, Long> determineValue) {
            this.id = id;
            this.determineValue = determineValue;
        }

        public static Type of(int typeId) {
            for (Type type : values()) {
                if (type.id == typeId) {
                    return type;
                }
            }
            return null;
        }

        public Function<Packet, Long> getDetermineValue() {
            return determineValue;
        }
    }

    public static class LiteralValuePacket extends Packet {
        private final long literalValue;

        public LiteralValuePacket(int version, int typeId, Type type, long literalValue) {
            super(version, typeId, type);
            this.literalValue = literalValue;
        }

        @Override
        public int calculateVersion() {
            return getVersion();
        }

        public long getLiteralValue() {
            return literalValue;
        }
    }

    public static class OperatorPacket extends Packet {
        private final List<Packet> subPackets;

        public OperatorPacket(int version, int typeId, Type type, List<Packet> subPackets) {
            super(version, typeId, type);
            this.subPackets = subPackets;
        }

        @Override
        public int calculateVersion() {
            return getVersion() + subPackets.stream().mapToInt(Packet::calculateVersion).sum();
        }

        public List<Packet> getSubPackets() {
            return subPackets;
        }
    }

    private final int version;
    private final int typeId;
    private final Packet.Type type;

    public Packet(int version, int typeId, Type type) {
        this.version = version;
        this.typeId = typeId;
        this.type = type;
    }

    public abstract int calculateVersion();

    public long calculateValue() {
        return type.getDetermineValue().apply(this);
    }

    public int getVersion() {
        return version;
    }

    public int getTypeId() {
        return typeId;
    }

    public Type getType() {
        return type;
    }
}
