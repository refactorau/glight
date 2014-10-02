package au.com.refactor

public enum Colour {
    BLACK((byte)0xFF),
    GREEN((byte)0xFE), // depending on device might also be blue
    RED ((byte)0xFD),
    BLUE((byte)0xFB);

    private byte code

    private Colour(byte c) {
        code = c
    }

    public byte getCode() {
        return code
    }
}