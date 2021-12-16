package nl.ricoapon.day16;

public class MessageReader {
    private final String inputAsBinary;
    private int index = 0;

    public MessageReader(String hexadecimalInput) {
        this.inputAsBinary = parseHexadecimalInput(hexadecimalInput);
    }

    private String parseHexadecimalInput(String hexadecimalInput) {
        StringBuilder stringBuilder = new StringBuilder();
        hexadecimalInput.chars().forEach(c -> {
            int value = Integer.parseInt(String.valueOf((char) c), 16);
            stringBuilder.append(convertToBinaryStringWithLength4(value));
        });

        return stringBuilder.toString();
    }

    private String convertToBinaryStringWithLength4(int value) {
        return String.format("%4s", Integer.toBinaryString(value))
                .replace(' ', '0');
    }

    public String readNBits(int n) {
        index += n;
        return inputAsBinary.substring(index - n, index);
    }

    public String readTheRest() {
        return inputAsBinary.substring(index);
    }
}
